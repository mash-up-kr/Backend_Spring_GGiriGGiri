package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import mashup.ggiriggiri.gifticonstorm.application.CouponService
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.common.error.exception.EntityNotFoundException
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.dto.event.CreateEventRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.GetSprinkleResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.SprinkleInfoResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.SprinkleRegistHistoryResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.GetSprinkleVo
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime


@Service
class SprinkleService(
    private val sprinkleRepository: SprinkleRepository,
    private val participantRepository: ParticipantRepository,
    private val couponService: CouponService,
    private val memberRepository: MemberRepository,
) {

    companion object : Logger

    fun getSprinkles(
        userInfoDto: UserInfoDto, orderBy: OrderBy?, category: Category?, noOffsetRequest: NoOffsetRequest
    ): List<GetSprinkleResDto> {
        if (orderBy == null || category == null) {
            throw BaseException(ResponseCode.INVALID_INPUT_VALUE)
        }
        val getSprinkleVos =
            if (orderBy == OrderBy.DEADLINE) findAllByDeadLine(category)
            else findAllByCategory(category, noOffsetRequest)
        val sprinkleIds = participantRepository.findAllSprinkleIdByMemberId(userInfoDto.id)
        return getSprinkleVos
            .filter { !it.isSprinkled() }
            .map { GetSprinkleResDto.of(it, it.sprinkleId in sprinkleIds) }
    }

    private fun findAllByCategory(category: Category, noOffsetRequest: NoOffsetRequest): List<GetSprinkleVo> {
        return sprinkleRepository.findAllByCategory(category, noOffsetRequest)
    }

    private fun findAllByDeadLine(category: Category): List<GetSprinkleVo> {
        if (category != Category.ALL) throw BaseException(ResponseCode.INVALID_INPUT_VALUE)
        return sprinkleRepository.findAllByDeadLine(10, 4)
    }

    @Transactional
    fun createSprinkle(image: MultipartFile, createEventRequestDto: CreateEventRequestDto, userInfo: UserInfoDto) {
        val member = memberRepository.findByInherenceId(userInfo.inherenceId) ?: throw BaseException(ResponseCode.DATA_NOT_FOUND, "member not found -> inherenceId : ${userInfo.inherenceId}")

        // coupon 저장
        val coupon = couponService.saveCoupon(image, createEventRequestDto, member)

        // sprinkle 저장
        val sprinkle = saveSprinkle(createEventRequestDto, coupon, member)
        log.info("sprinkledAt ${sprinkle.sprinkleAt}, deadline minutes ${createEventRequestDto.deadlineMinutes}")
    }

    fun saveSprinkle(createEventRequestDto: CreateEventRequestDto, coupon: Coupon, user: Member): Sprinkle {
        val entity = Sprinkle.of(deadlineMinutes = createEventRequestDto.deadlineMinutes, coupon = coupon, member = user)
        return sprinkleRepository.save(entity)
    }

    @Transactional
    fun applySprinkle(userInfoDto: UserInfoDto, sprinkleId: Long) {
        val applySprinkleMember = memberRepository.findByInherenceId(userInfoDto.inherenceId) ?: throw BaseException(ResponseCode.DATA_NOT_FOUND, "member not found -> inherenceId : ${userInfoDto.inherenceId}")
        val sprinkle = sprinkleRepository.findByIdOrNull(sprinkleId) ?: throw BaseException(ResponseCode.DATA_NOT_FOUND, "sprinkle not found -> sprinkleId : $sprinkleId")

        if (sprinkle.sprinkleAt.isBefore(LocalDateTime.now()))
            throw BaseException(ResponseCode.ALREADY_EXPIRED_SPRINKLE, "만료된 뿌리기 -> sprinkleId : ${sprinkle.id}, memberId : ${applySprinkleMember.id}")

        if (sprinkle.member.id == applySprinkleMember.id)
            throw BaseException(ResponseCode.INVALID_PARTICIPATE_REQUEST, "뿌리기 생성자, 참여자 동일 -> sprinkleId : ${sprinkle.id}, memberId : ${applySprinkleMember.id}")

        if (sprinkle.participants.any { it.member.id == applySprinkleMember.id })
            throw BaseException(ResponseCode.ALREADY_PARTICIPATE_IN, "이미 참여한 뿌리기 sprinkleId : ${sprinkle.id}, memberId : ${applySprinkleMember.id}")

        try {
            participantRepository.save(Participant(member = applySprinkleMember, sprinkle = sprinkle))
        } catch (e: DataIntegrityViolationException) {
            throw BaseException(ResponseCode.ALREADY_PARTICIPATE_IN, "이미 참여한 뿌리기 sprinkleId : ${sprinkle.id}, memberId : ${applySprinkleMember.id}")
        }
    }

    fun getSprinkleInfo(sprinkleId: Long, userInfoDto: UserInfoDto): SprinkleInfoResDto {
        val sprinkleInfoVo = sprinkleRepository.findInfoById(sprinkleId)
            ?: throw EntityNotFoundException("sprinkle", "sprinkleId : $sprinkleId")
        val memberIds = participantRepository.findAllMemberIdBySprinkleId(sprinkleId)
        return SprinkleInfoResDto.of(sprinkleInfoVo, userInfoDto, memberIds.size, userInfoDto.id in memberIds)
    }

    fun getSprinkleRegistHistory(userInfoDto: UserInfoDto, noOffsetRequest: NoOffsetRequest): List<SprinkleRegistHistoryResDto> {
        val registHistoryVos = sprinkleRepository.findRegistHistoryByMemberId(userInfoDto.id, noOffsetRequest)
        return registHistoryVos.map { SprinkleRegistHistoryResDto.of(it) }
    }

}
package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import mashup.ggiriggiri.gifticonstorm.application.CouponService
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.dto.event.CreateEventRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.GetSprinkleResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleListVo
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Service
class SprinkleService(
    private val sprinkleCache: SprinkleCache,
    private val sprinkleRepository: SprinkleRepository,
    private val participantRepository: ParticipantRepository,
    private val couponService: CouponService,
    private val memberRepository: MemberRepository,
) {
    fun getSprinkles(orderBy: OrderBy?, category: Category?, noOffsetRequest: NoOffsetRequest): List<GetSprinkleResDto> {
        if (orderBy == null || category == null) {
            throw BaseException(ResponseCode.INVALID_INPUT_VALUE)
        }
        val sprinkleListVos =
            if (orderBy == OrderBy.DEADLINE) findAllByDeadLine(category)
            else findAllByCategory(category, noOffsetRequest)
        val sprinkleIds = participantRepository.findAllSprinkleIdByMemberId(1) //TODO: 로그인 기능 추가 후 해당 사용자 id 전달
        return sprinkleListVos.map { GetSprinkleResDto.toDto(it, sprinkleIds) }
    }

    private fun findAllByCategory(category: Category, noOffsetRequest: NoOffsetRequest): List<SprinkleListVo> {
        return sprinkleRepository.findAllByCategory(category, noOffsetRequest)
    }

    private fun findAllByDeadLine(category: Category): List<SprinkleListVo> {
        if (category != Category.ALL) throw BaseException(ResponseCode.INVALID_INPUT_VALUE)
        return sprinkleRepository.findAllByDeadLine(10, 4)
    }

    fun createSprinkle(image: MultipartFile, dto: CreateEventRequestDto, userInfo: UserInfoDto) {
        // throw 잘 하기
        val user = memberRepository.findById(userInfo.id).orElseThrow { throw Exception() }

        // coupon 저장
        val coupon = couponService.saveCoupon(image, dto, user)

        // sprinkle 저장
        val sprinkle = saveSprinkle(dto, coupon, user)

        // redis에 sprinkle 저장
        // TODO 종현이형


    }

    fun saveSprinkle(dto: CreateEventRequestDto, coupon: Coupon, user: Member): Sprinkle {
        val entity = Sprinkle(
            sprinkleAt = getSprinkleTimeFromNow(dto.sprinkleTime),
            coupon = coupon,
            member = user,
        )
        return sprinkleRepository.save(entity)
    }

    private fun getSprinkleTimeFromNow(sprinkleTime: String) : LocalDateTime {
        return LocalDateTime.now().plusMinutes(sprinkleTime.toLong())
    }

}
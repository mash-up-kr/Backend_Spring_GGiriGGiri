package mashup.ggiriggiri.gifticonstorm.application.participant

import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.participant.dto.ParticipantInfoResDto
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.DrawResultResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ParticipantService(
    private val participantRepository: ParticipantRepository,
    private val sprinkleRepository: SprinkleRepository
) {

    fun getParticipantHistory(userInfoDto: UserInfoDto, noOffsetRequest: NoOffsetRequest): List<ParticipantInfoResDto> {
        val participantInfoVos = participantRepository.findHistoryByMemberId(userInfoDto.id, noOffsetRequest)
        return participantInfoVos.map { ParticipantInfoResDto.of(it) }
    }

    @Transactional
    fun getDrawResult(userInfo: UserInfoDto, sprinkleId: Long): DrawResultResDto {
        val sprinkle = sprinkleRepository.findByIdOrNull(sprinkleId) ?: throw BaseException(ResponseCode.DATA_NOT_FOUND, "sprinkle not found -> sprinkleId : $sprinkleId")
        val participant = sprinkle.participants.firstOrNull { participant ->
            participant.member.id == userInfo.id
        } ?: throw BaseException(ResponseCode.DATA_NOT_FOUND, "참여하지 않은 뿌리기 -> memberId : ${userInfo.id} sprinkleId : $sprinkleId")

        participant.drawResultCheck()

        return DrawResultResDto.of(participant.drawStatus, sprinkle.coupon)
    }

}
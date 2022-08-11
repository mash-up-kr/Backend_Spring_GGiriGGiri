package mashup.ggiriggiri.gifticonstorm.application.participant

import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.participant.dto.ParticipantInfoResDto
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import org.springframework.stereotype.Service

@Service
class ParticipantService(
    private val participantRepository: ParticipantRepository
) {

    fun getParticipantHistory(userInfoDto: UserInfoDto, noOffsetRequest: NoOffsetRequest): List<ParticipantInfoResDto> {
        val participantInfoVos = participantRepository.findHistoryByMemberId(userInfoDto.id, noOffsetRequest)
        return participantInfoVos.map { ParticipantInfoResDto.of(it) }
    }

}
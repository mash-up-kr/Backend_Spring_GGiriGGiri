package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.application.participant.ParticipantService
import mashup.ggiriggiri.gifticonstorm.common.dto.BaseResponse
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.config.annotation.UserInfo
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.participant.dto.ParticipantInfoResDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class ParticipantController(
    private val participantService: ParticipantService
) {

    @GetMapping("/participant/history")
    fun getParticipantHistory(
        @UserInfo userInfoDto: UserInfoDto,
        noOffsetRequest: NoOffsetRequest
    ): BaseResponse<List<ParticipantInfoResDto>> {
        return BaseResponse.ok(
            participantService.getParticipantHistory(userInfoDto, noOffsetRequest)
        )
    }
}
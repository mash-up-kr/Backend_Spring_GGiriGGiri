package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.application.participant.ParticipantService
import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.common.dto.BaseResponse
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.config.annotation.UserInfo
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.participant.dto.ParticipantInfoResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.CouponInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.DrawResultResDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import kotlin.random.Random

@RestController
@RequestMapping("/api/v1/participant")
class ParticipantController(
    private val participantService: ParticipantService
) {

    @GetMapping("/history")
    fun getParticipantHistory(
        @UserInfo userInfoDto: UserInfoDto,
        noOffsetRequest: NoOffsetRequest
    ): BaseResponse<List<ParticipantInfoResDto>> {
        return BaseResponse.ok(
            participantService.getParticipantHistory(userInfoDto, noOffsetRequest)
        )
    }

    @GetMapping("/draw-result/{sprinkleId}")
    fun getDrawResult(@UserInfo userInfoDto: UserInfoDto, @PathVariable sprinkleId: Long): BaseResponse<DrawResultResDto> {
        return BaseResponse.ok(participantService.getDrawResult(userInfoDto, sprinkleId))
    }
}
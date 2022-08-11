package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

import com.fasterxml.jackson.annotation.JsonInclude
import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DrawResultResDto(
    val drawStatus: DrawStatus,
    val couponInfo: CouponInfoDto? = null
)

data class CouponInfoDto(
    val brandName: String,
    val merchandiseName: String,
    val expiredAt: LocalDateTime,
    val imageUrl: String
)

package mashup.ggiriggiri.gifticonstorm.domain.dto.event

import mashup.ggiriggiri.gifticonstorm.common.annotation.SprinkleTimeCheck
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@SprinkleTimeCheck
data class CreateEventRequestDto(
    val category: Category,
    @field:NotBlank
    val brandName: String,
    @field:NotBlank
    val merchandiseName: String,
    @field:NotBlank
    val couponExpiredTime: String,
    @field:Min(value = 0, message = "The deadlineMinutes must be positive")
    val deadlineMinutes: Long,
)

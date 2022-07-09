package mashup.ggiriggiri.gifticonstorm.domain.coupon.dto

import mashup.ggiriggiri.gifticonstorm.common.annotation.CouponTimeCheck
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@CouponTimeCheck
data class CouponSaveRequestDto(
    val category: Category,

    @field:NotBlank
    val brandName: String,

    @field:NotBlank
    val merchandiseName: String,

    @field:NotBlank
    val couponExpiredTime: String,

    @field:Min(value = 1) @field:Max(value = 24)
    val sprinkleTime: Long
)
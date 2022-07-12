package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category

data class GetSprinkleResDto(
    val brandName: String,
    val merchandiseName: String,
    val category: Category,
    val expiredAt: String,
    val participants: Int,
    val sprinkleAt: String,
    val participateIn: Boolean
)
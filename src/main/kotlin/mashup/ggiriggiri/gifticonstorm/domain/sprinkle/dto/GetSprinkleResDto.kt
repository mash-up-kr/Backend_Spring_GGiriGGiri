package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.GetSprinkleVo

data class GetSprinkleResDto(
    val sprinkleId: Long,
    val brandName: String,
    val merchandiseName: String,
    val category: Category,
    val expiredAt: String,
    val participants: Int,
    val sprinkleAt: String,
    val participateIn: Boolean
) {

    companion object {
        fun of(getSprinkleVo: GetSprinkleVo, participateIn: Boolean): GetSprinkleResDto {
            return GetSprinkleResDto(
                sprinkleId = getSprinkleVo.sprinkleId,
                brandName = getSprinkleVo.brandName,
                merchandiseName = getSprinkleVo.merchandiseName,
                category = getSprinkleVo.category,
                expiredAt = getSprinkleVo.expiredAt.toString(),
                participants = getSprinkleVo.participants,
                sprinkleAt = getSprinkleVo.sprinkleAt.toString(),
                participateIn = participateIn
            )
        }
    }
}
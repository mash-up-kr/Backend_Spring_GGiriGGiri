package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleInfoVo

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
        fun of(sprinkleInfoVo: SprinkleInfoVo, participateIn: Boolean): GetSprinkleResDto {
            return GetSprinkleResDto(
                sprinkleId = sprinkleInfoVo.sprinkleId,
                brandName = sprinkleInfoVo.brandName,
                merchandiseName = sprinkleInfoVo.merchandiseName,
                category = sprinkleInfoVo.category,
                expiredAt = sprinkleInfoVo.expiredAt.toString(),
                participants = sprinkleInfoVo.participants,
                sprinkleAt = sprinkleInfoVo.sprinkleAt.toString(),
                participateIn = participateIn
            )
        }
    }
}
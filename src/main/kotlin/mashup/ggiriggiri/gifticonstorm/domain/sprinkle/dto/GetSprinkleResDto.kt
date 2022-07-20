package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleListVo

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
        fun toDto(sprinkleListVo: SprinkleListVo, sprinkleIds: List<Long>): GetSprinkleResDto {
            return GetSprinkleResDto(
                sprinkleId = sprinkleListVo.sprinkleId,
                brandName = sprinkleListVo.brandName,
                merchandiseName = sprinkleListVo.merchandiseName,
                category = sprinkleListVo.category,
                expiredAt = sprinkleListVo.expiredAt.toString(),
                participants = sprinkleListVo.participants,
                sprinkleAt = sprinkleListVo.sprinkleAt.toString(),
                participateIn = sprinkleListVo.sprinkleId in sprinkleIds
            )

        }
    }
}
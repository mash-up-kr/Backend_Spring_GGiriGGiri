package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleRegistHistoryVo

data class SprinkleRegistHistoryResDto(
    val sprinkleId: Long,
    val brandName: String,
    val merchandiseName: String,
    val expiredAt: String,
    val category: Category,
    val participants: Int,
    val deliveryDate: String,
    val sprinkledStatus: SprinkledStatus
) {

    companion object {
        fun of(sprinkleRegistHistoryVo: SprinkleRegistHistoryVo): SprinkleRegistHistoryResDto {
            return SprinkleRegistHistoryResDto(
                sprinkleId = sprinkleRegistHistoryVo.sprinkleId,
                brandName = sprinkleRegistHistoryVo.brandName,
                merchandiseName = sprinkleRegistHistoryVo.merchandiseName,
                expiredAt = sprinkleRegistHistoryVo.expiredAt.toString(),
                category = sprinkleRegistHistoryVo.category,
                participants = sprinkleRegistHistoryVo.participants,
                deliveryDate = sprinkleRegistHistoryVo.sprinkleAt.toString(),
                sprinkledStatus = sprinkleRegistHistoryVo.getSprinkledStatus()
            )
        }
    }
}

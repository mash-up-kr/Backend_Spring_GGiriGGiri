package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleInfoVo

data class SprinkleInfoResDto(
    val sprinkleId: Long,
    val brandName: String,
    val merchandiseName: String,
    val category: Category,
    val expiredAt: String,
    val participants: Int,
    val sprinkleAt: String
) {

    companion object {
        fun of(sprinkleInfoVo: SprinkleInfoVo): SprinkleInfoResDto {
            return SprinkleInfoResDto(
                sprinkleId = sprinkleInfoVo.sprinkleId,
                brandName = sprinkleInfoVo.brandName,
                merchandiseName = sprinkleInfoVo.merchandiseName,
                category = sprinkleInfoVo.category,
                expiredAt = sprinkleInfoVo.expiredAt.toString(),
                participants = sprinkleInfoVo.participants,
                sprinkleAt = sprinkleInfoVo.sprinkleAt.toString()
            )
        }
    }
}

package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleInfoVo

data class SprinkleInfoResDto(
    val sprinkleId: Long,
    val brandName: String,
    val merchandiseName: String,
    val category: String,
    val expiredAt: String,
    val participants: Int,
    val sprinkleAt: String,
    val registeredBy: Boolean
) {

    companion object {
        fun of(sprinkleInfoVo: SprinkleInfoVo, userInfoDto: UserInfoDto): SprinkleInfoResDto {
            return SprinkleInfoResDto(
                sprinkleId = sprinkleInfoVo.sprinkleId,
                brandName = sprinkleInfoVo.brandName,
                merchandiseName = sprinkleInfoVo.merchandiseName,
                category = Category.getDescription(sprinkleInfoVo.category.name),
                expiredAt = sprinkleInfoVo.expiredAt.toString(),
                participants = sprinkleInfoVo.participants,
                sprinkleAt = sprinkleInfoVo.sprinkleAt.toString(),
                registeredBy = sprinkleInfoVo.getRegisteredBy(userInfoDto)
            )
        }
    }
}

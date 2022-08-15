package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo

import com.querydsl.core.annotations.QueryProjection
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import java.time.LocalDateTime

data class SprinkleInfoVo @QueryProjection constructor(
    val sprinkleId: Long,
    val brandName: String,
    val merchandiseName: String,
    val category: Category,
    val expiredAt: LocalDateTime,
    val sprinkleAt: LocalDateTime,
    val memberId: Long
) {

    fun getRegisteredBy(userInfoDto: UserInfoDto): Boolean {
        return memberId == userInfoDto.id
    }
}
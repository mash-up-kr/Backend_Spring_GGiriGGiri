package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo

import com.querydsl.core.annotations.QueryProjection
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import org.apache.tomcat.jni.Local
import java.time.LocalDateTime

data class GetSprinkleVo @QueryProjection constructor(
    val sprinkleId: Long,
    val brandName: String,
    val merchandiseName: String,
    val category: Category,
    val expiredAt: LocalDateTime,
    val participants: Int,
    val sprinkleAt: LocalDateTime
) {
    fun isSprinkled(): Boolean {
        val now = LocalDateTime.now()
        return sprinkleAt.isBefore(now)
    }
}

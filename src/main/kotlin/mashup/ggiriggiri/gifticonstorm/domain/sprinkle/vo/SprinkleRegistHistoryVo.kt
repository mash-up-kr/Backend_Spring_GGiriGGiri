package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo

import com.querydsl.core.annotations.QueryProjection
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import java.time.LocalDateTime

data class SprinkleRegistHistoryVo @QueryProjection constructor(
    val sprinkleId: Long,
    val brandName: String,
    val merchandiseName: String,
    val expiredAt: LocalDateTime,
    val category: Category,
    val participants: Int,
    val sprinkled: Boolean,
    val sprinkleAt: LocalDateTime
)
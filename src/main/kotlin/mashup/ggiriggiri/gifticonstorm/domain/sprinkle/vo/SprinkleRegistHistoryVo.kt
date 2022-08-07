package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo

import com.querydsl.core.annotations.QueryProjection
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.SprinkledStatus
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
) {

    fun getSprinkledStatus(): SprinkledStatus {
        if (!sprinkled) return SprinkledStatus.PROGRESS
        return if (participants > 0) SprinkledStatus.FINISH else SprinkledStatus.NO_PARTICIPANTS
    }
}
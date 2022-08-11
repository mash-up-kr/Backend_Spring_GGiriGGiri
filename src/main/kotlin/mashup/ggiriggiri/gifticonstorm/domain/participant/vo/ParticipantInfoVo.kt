package mashup.ggiriggiri.gifticonstorm.domain.participant.vo

import com.querydsl.core.annotations.QueryProjection
import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import java.time.LocalDateTime

data class ParticipantInfoVo @QueryProjection constructor(
    val participantId: Long,
    val sprinkleId: Long,
    val brandName: String,
    val merchandiseName: String,
    val expiredAt: LocalDateTime,
    val category: Category,
    val participants: Int,
    val createdAt: LocalDateTime,
    val isChecked: Boolean,
    val drawStatus: DrawStatus
)
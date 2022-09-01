package mashup.ggiriggiri.gifticonstorm.domain.participant.dto

import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.participant.vo.ParticipantInfoVo

data class ParticipantInfoResDto(
    val participantId: Long,
    val sprinkleId: Long,
    val brandName: String,
    val merchandiseName: String,
    val expiredAt: String,
    val category: Category,
    val participants: Int,
    val participateDate: String,
    val isChecked: Boolean,
    val drawStatus: DrawStatus,
) {

    companion object {
        fun of(participantInfoVo: ParticipantInfoVo): ParticipantInfoResDto {
            return ParticipantInfoResDto(
                participantId = participantInfoVo.participantId,
                sprinkleId = participantInfoVo.sprinkleId,
                brandName = participantInfoVo.brandName,
                merchandiseName = participantInfoVo.merchandiseName,
                expiredAt = participantInfoVo.expiredAt.toString(),
                category = participantInfoVo.category,
                participants = participantInfoVo.participants,
                participateDate = participantInfoVo.createdAt.toString(),
                isChecked = participantInfoVo.isChecked,
                drawStatus = participantInfoVo.drawStatus
            )
        }
    }
}

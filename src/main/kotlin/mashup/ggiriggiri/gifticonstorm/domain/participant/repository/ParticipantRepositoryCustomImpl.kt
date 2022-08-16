package mashup.ggiriggiri.gifticonstorm.domain.participant.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.QCoupon.coupon
import mashup.ggiriggiri.gifticonstorm.domain.participant.QParticipant.participant
import mashup.ggiriggiri.gifticonstorm.domain.participant.vo.ParticipantInfoVo
import mashup.ggiriggiri.gifticonstorm.domain.participant.vo.QParticipantInfoVo
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.QSprinkle.sprinkle

class ParticipantRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
): ParticipantRepositoryCustom {

    override fun findAllSprinkleIdByMemberId(memberId: Long): List<Long> {
        return jpaQueryFactory
            .select(participant.sprinkle.id)
            .from(participant)
            .where(participant.member.id.eq(memberId))
            .fetch()
    }

    override fun findHistoryByMemberId(memberId: Long, noOffsetRequest: NoOffsetRequest): List<ParticipantInfoVo> {
        return jpaQueryFactory
            .select(QParticipantInfoVo(
                participant.id,
                sprinkle.id,
                coupon.brandName,
                coupon.merchandiseName,
                coupon.expiredAt,
                coupon.category,
                sprinkle.participants.size(),
                participant.createdAt,
                participant.isChecked,
                participant.drawStatus
            ))
            .from(participant)
            .join(participant.sprinkle, sprinkle)
            .join(sprinkle.coupon, coupon)
            .where(
                ltParticipantId(noOffsetRequest.id),
                participant.member.id.eq(memberId)
            )
            .orderBy(participant.id.desc())
            .limit(noOffsetRequest.limit)
            .fetch()
    }

    override fun findAllMemberIdBySprinkleId(sprinkleId: Long): List<Long> {
        return jpaQueryFactory
            .select(participant.member.id)
            .from(participant)
            .where(participant.sprinkle.id.eq(sprinkleId))
            .fetch()
    }

    private fun ltParticipantId(id: Long?): BooleanExpression? {
        return id?.let { participant.id.lt(id) }
    }
}
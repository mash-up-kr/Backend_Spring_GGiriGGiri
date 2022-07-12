package mashup.ggiriggiri.gifticonstorm.domain.participant.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import mashup.ggiriggiri.gifticonstorm.domain.participant.QParticipant.participant

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
}
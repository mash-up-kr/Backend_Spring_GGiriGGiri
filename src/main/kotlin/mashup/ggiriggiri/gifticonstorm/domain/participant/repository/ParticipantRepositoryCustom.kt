package mashup.ggiriggiri.gifticonstorm.domain.participant.repository

interface ParticipantRepositoryCustom {

    fun findAllSprinkleIdByMemberId(memberId: Long): List<Long>
}
package mashup.ggiriggiri.gifticonstorm.domain.participant.repository

import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import org.springframework.data.jpa.repository.JpaRepository

interface ParticipantRepository: JpaRepository<Participant, Long>, ParticipantRepositoryCustom
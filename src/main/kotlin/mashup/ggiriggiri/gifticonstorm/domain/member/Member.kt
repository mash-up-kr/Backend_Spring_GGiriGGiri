package mashup.ggiriggiri.gifticonstorm.domain.member

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Member (
        @Column(unique = true)
        val inherenceId: String,

        @OneToMany(mappedBy = "member")
        val participants: MutableList<Participant> = mutableListOf()
) : BaseEntity()
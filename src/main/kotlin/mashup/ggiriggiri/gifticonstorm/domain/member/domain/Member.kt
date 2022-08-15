package mashup.ggiriggiri.gifticonstorm.domain.member.domain

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import javax.persistence.*

@Entity
class Member (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @Column(unique = true)
    val inherenceId: String,

    @OneToMany(mappedBy = "member")
    val participants: MutableList<Participant> = mutableListOf()
) : BaseEntity()
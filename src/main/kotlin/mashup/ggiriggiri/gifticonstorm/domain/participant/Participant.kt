package mashup.ggiriggiri.gifticonstorm.domain.participant

import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Participant(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprinkle_id")
    val sprinkle: Sprinkle,

    @Enumerated(EnumType.STRING)
    val drawStatus: DrawStatus = DrawStatus.PROGRESS,

    var isChecked: Boolean = false,

    var checkedAt: LocalDateTime? = null
) : BaseEntity()
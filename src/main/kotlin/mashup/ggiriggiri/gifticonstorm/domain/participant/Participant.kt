package mashup.ggiriggiri.gifticonstorm.domain.participant

import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.member.Member
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import javax.persistence.*

@Entity
class Participant(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sprinkle_id")
    val sprinkle: Sprinkle,

    @Enumerated(EnumType.STRING)
    val drawStatus: DrawStatus = DrawStatus.PROGRESS
) : BaseEntity()
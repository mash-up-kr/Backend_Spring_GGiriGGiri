package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Sprinkle(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member,
    @OneToOne
    @JoinColumn(name = "coupon_id")
    val coupon: Coupon,
    val sprinkleAt: LocalDateTime,
    var sprinkled: Boolean = false,
    @OneToMany(mappedBy = "sprinkle")
    val participants: MutableList<Participant> = mutableListOf()
) : BaseEntity() {

    companion object : Logger {

        fun of(deadlineMinutes: Long, coupon: Coupon, member: Member): Sprinkle {
            return Sprinkle(sprinkleAt = getSprinkleTimeFromNow(deadlineMinutes), coupon = coupon, member = member)
        }

        private fun getSprinkleTimeFromNow(deadlineMinutes: Long): LocalDateTime {
            log.info("getSprinkleTimeFromNow deadlineMinutes : $deadlineMinutes")
            val dateTime = LocalDateTime.now().plusMinutes(deadlineMinutes)
            log.info("getSprinkleTimeFromNow plusedMinutes  : $dateTime")
            return dateTime
        }
    }

    fun drawProcess() {
        if (!sprinkled) {
            this.sprinkled = true
            draw()
        }
    }

    private fun draw() {
        if (participants.isEmpty()) return
        val winnerParticipant = participants.also { it.shuffle() }.first()
        val loserParticipants = participants.subList(1, participants.count())
        winnerParticipant.win()
        log.info("winner id : ${winnerParticipant.id}")
        loserParticipants.forEach { it.lose() }
    }
}
package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Sprinkle(
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
    companion object {
        fun of(deadlineMinutes: Long, coupon: Coupon, member: Member): Sprinkle {
            return Sprinkle(sprinkleAt = getSprinkleTimeFromNow(deadlineMinutes), coupon = coupon, member = member)
        }

        private fun getSprinkleTimeFromNow(deadlineMinutes: Long): LocalDateTime {
            return LocalDateTime.now().plusMinutes(deadlineMinutes)
        }
    }


}
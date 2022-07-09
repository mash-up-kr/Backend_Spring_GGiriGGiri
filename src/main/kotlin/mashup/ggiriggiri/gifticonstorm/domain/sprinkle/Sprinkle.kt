package mashup.ggiriggiri.gifticonstorm.domain.sprinkle

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.member.Member
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

@Entity
class Sprinkle(
    @ManyToOne
    @JoinColumn(name = "member_id")
    val member: Member,
    @OneToOne
    @JoinColumn(name = "coupon_id")
    val coupon: Coupon,
    val sprinkleAt: LocalDateTime,
    var sprinkled: Boolean = false
) : BaseEntity()
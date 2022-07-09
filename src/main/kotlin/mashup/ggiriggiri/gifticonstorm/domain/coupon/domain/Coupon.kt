package mashup.ggiriggiri.gifticonstorm.domain.coupon.domain

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.coupon.dto.CouponSaveRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToMany

@Entity
class Coupon (
    val brandName: String,
    val merchandiseName: String,
    val expiredAt: LocalDateTime,
    val sprinkleAt: LocalDateTime,
    val imageUrl: String,

    @Enumerated(EnumType.STRING)
    val category: Category,

    @OneToMany(mappedBy = "coupon")
    val participants: MutableList<Participant> = mutableListOf()
): BaseEntity() {

    companion object {
        fun of(imageUrl: String, couponSaveRequestDto: CouponSaveRequestDto): Coupon {
            return Coupon(
                brandName = couponSaveRequestDto.brandName,
                merchandiseName = couponSaveRequestDto.merchandiseName,
                expiredAt = LocalDate.parse(couponSaveRequestDto.couponExpiredTime).atTime(LocalTime.MAX),
                sprinkleAt = LocalDateTime.now().plusHours(couponSaveRequestDto.sprinkleTime),
                imageUrl = imageUrl,
                category = couponSaveRequestDto.category
            )
        }
    }
}
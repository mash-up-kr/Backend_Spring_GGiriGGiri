package mashup.ggiriggiri.gifticonstorm.domain.coupon.domain

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.coupon.dto.CouponSaveRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.member.Member
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.*

@Entity
class Coupon(
    val brandName: String,
    val merchandiseName: String,
    val expiredAt: LocalDateTime,
    val imageUrl: String,

    @Enumerated(EnumType.STRING)
    val category: Category,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member
): BaseEntity() {

    companion object {
        fun of(imageUrl: String, couponSaveRequestDto: CouponSaveRequestDto): Coupon {
            return Coupon(
                brandName = couponSaveRequestDto.brandName,
                merchandiseName = couponSaveRequestDto.merchandiseName,
                expiredAt = LocalDate.parse(couponSaveRequestDto.couponExpiredTime).atTime(LocalTime.MAX),
                imageUrl = imageUrl,
                category = couponSaveRequestDto.category,
                member = Member("sample") // TODO : 임시 멤버 이후에 조회하여 실제 멤버 엔티티로 대체
            )
        }
    }
}
package mashup.ggiriggiri.gifticonstorm.domain.coupon.domain

import mashup.ggiriggiri.gifticonstorm.domain.BaseEntity
import mashup.ggiriggiri.gifticonstorm.domain.dto.event.CreateEventRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.*

@Entity
class Coupon(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
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
        fun of(imageUrl: String, createEventRequestDto: CreateEventRequestDto, member: Member): Coupon {
            return Coupon(
                brandName = createEventRequestDto.brandName,
                merchandiseName = createEventRequestDto.merchandiseName,
                expiredAt = LocalDate.parse(createEventRequestDto.couponExpiredTime).atTime(23, 59, 59),
                imageUrl = imageUrl,
                category = createEventRequestDto.category,
                member = member
            )
        }
    }
}
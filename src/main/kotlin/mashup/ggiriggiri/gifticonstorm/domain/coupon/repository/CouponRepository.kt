package mashup.ggiriggiri.gifticonstorm.domain.coupon.repository

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository: JpaRepository<Coupon, Long> {
}
package mashup.ggiriggiri.gifticonstorm.domain.coupon.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.QCoupon
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.QMember

import mashup.ggiriggiri.gifticonstorm.domain.participant.QParticipant
import org.springframework.data.jpa.repository.JpaRepository

interface CouponRepository: JpaRepository<Coupon, Long>, CouponRepositoryCustom

interface CouponRepositoryCustom {
    fun findByCouponId(couponId: Long): Coupon?
}

class CouponRepositoryCustomImpl(private val jpaQueryFactory: JPAQueryFactory) : CouponRepositoryCustom {
    override fun findByCouponId(couponId: Long): Coupon? {
        val qCoupon = QCoupon.coupon
        val qParticipant = QParticipant.participant
        val qMember = QMember.member

        return jpaQueryFactory.selectFrom(qCoupon)
//            .leftJoin(qCoupon.participants, qParticipant).fetchJoin()
            .join(qParticipant.member, qMember).fetchJoin()
            .where(qCoupon.id.eq(couponId))
            .fetchOne()
    }
}
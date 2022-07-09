package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import mashup.ggiriggiri.gifticonstorm.application.push.PushService
import mashup.ggiriggiri.gifticonstorm.domain.coupon.repository.CouponRepository
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class SprinkleDrawHandler(
    private val couponRepository: CouponRepository,
    private val pushService: PushService
) {

    companion object : Logger

    @Async
    @EventListener
    fun drawEventListener(sprinkleDto: SprinkleDto) {
        draw(sprinkleDto.couponId)
    }

    private fun draw(couponId: Long) {
        val coupon = couponRepository.findByCouponId(couponId) ?: throw RuntimeException()
        val winnerParticipant = coupon.participants.also { it.shuffle() }.first()
    }
}

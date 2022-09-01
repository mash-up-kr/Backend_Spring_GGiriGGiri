package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SprinkleDrawHandler(
    private val sprinkleRepository: SprinkleRepository,
) {

    companion object : Logger

    @Transactional
    @Scheduled(cron = "0 * * * * *", zone = "Asia/Seoul")
    fun drawEventListener() {
        val sprinkles = sprinkleRepository.findAllBySprinkledFalse()
        if (sprinkles.isEmpty()) {
            log.info("[DRAW] no sprinkles")
            return
        }

        sprinkles.forEach { it.drawProcess() }
        sprinkleRepository.saveAll(sprinkles)
        log.info("[DRAW] sprinkled items $sprinkles")
    }

}

package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import mashup.ggiriggiri.gifticonstorm.application.push.PushService
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.context.event.EventListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class SprinkleDrawHandler(
    private val sprinkleRepository: SprinkleRepository,
    private val pushService: PushService
) {

    companion object : Logger

    @Async
    @EventListener
    @Transactional
    fun drawEventListener(sprinkleDto: SprinkleDto) {
        draw(sprinkleDto.sprinkleId)
    }

    private fun draw(sprinkleId: Long) {
        val sprinkle = sprinkleRepository.findByIdOrNull(sprinkleId) ?: throw BaseException(ResponseCode.DATA_NOT_FOUND, "sprinkle not found -> sprinkleId : $sprinkleId")
        log.info("draw target sprinkle id : ${sprinkle.id}, sprinkleAt : ${sprinkle.sprinkleAt}, now : ${LocalDateTime.now()}")
        sprinkle.drawProcess()
    }
}

package mashup.ggiriggiri.gifticonstorm.application.bburigi

import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component

@Component
class BburigiDrawHandler {

    companion object : Logger

    @Async
    @EventListener
    fun draw(value: String) {
        // TODO: 추첨 기능 추가
        log.info("draw : $value")
    }
}
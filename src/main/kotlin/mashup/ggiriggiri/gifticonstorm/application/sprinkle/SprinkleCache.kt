package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import mashup.ggiriggiri.gifticonstorm.common.DEFAULT_OBJECT_MAPPER
import mashup.ggiriggiri.gifticonstorm.common.toJson
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.redisson.api.RedissonClient
import org.redisson.api.map.event.EntryEvent
import org.redisson.api.map.event.EntryExpiredListener
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Component
class SprinkleCache(
    private val eventPublisher: ApplicationEventPublisher,
    redissonClient: RedissonClient
) {
    companion object : Logger {
        const val SPRINKLE_KEY_IDENTIFIER = "sprinkle"
    }

    private val sprinkleCache = redissonClient.getMapCache<String, String>("sprinkle")

    @PostConstruct
    fun init() {
        sprinkleCache.addListener(object : EntryExpiredListener<String, String> {
            override fun onExpired(event: EntryEvent<String, String>) {
                if (event.key.startsWith(SPRINKLE_KEY_IDENTIFIER)) {
                    eventPublisher.publishEvent(DEFAULT_OBJECT_MAPPER.readValue(event.value, SprinkleDto::class.java))
                }
            }
        })
    }

    fun generateSprinkle(sprinkleId: Long, deadLineMinutes: Long) {
        val microseconds = deadLineMinutes * 60 * 1000 * 1000
        sprinkleCache.fastPut(generateSprinkleKey(), SprinkleDto(sprinkleId).toJson(), microseconds, TimeUnit.MICROSECONDS)
        log.info("generateSprinkle cache -> sprinkleId : $sprinkleId, deadLineMinutes : $deadLineMinutes, expireAt : ${LocalDateTime.now().plusMinutes(deadLineMinutes)}")
    }

    private fun generateSprinkleKey(): String {
        return "$SPRINKLE_KEY_IDENTIFIER:${UUID.randomUUID()}"
    }
}

data class SprinkleDto(
    val sprinkleId: Long
)
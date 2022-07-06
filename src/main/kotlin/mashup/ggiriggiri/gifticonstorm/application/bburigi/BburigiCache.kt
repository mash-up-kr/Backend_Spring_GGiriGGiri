package mashup.ggiriggiri.gifticonstorm.application.bburigi

import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.redisson.api.RedissonClient
import org.redisson.api.map.event.EntryEvent
import org.redisson.api.map.event.EntryExpiredListener
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import javax.annotation.PostConstruct

@Component
class BburigiCache(
    private val eventPublisher: ApplicationEventPublisher,
    redissonClient: RedissonClient
) {
    private val bburigiCache = redissonClient.getMapCache<String, String>("bburigi")

    companion object : Logger

    @PostConstruct
    fun init() {
        bburigiCache.addListener(object : EntryExpiredListener<String, String> {
            override fun onExpired(event: EntryEvent<String, String>) {
                eventPublisher.publishEvent(event.value)
            }
        })
    }

    fun generateBburigi(deadLineHour: Long) {
        // TODO: 추첨을 위한 뿌리기 생성기능 추가
        bburigiCache.fastPut("sample", "sample", deadLineHour, TimeUnit.HOURS)
    }
}
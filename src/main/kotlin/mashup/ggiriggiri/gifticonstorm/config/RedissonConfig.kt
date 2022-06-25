package mashup.ggiriggiri.gifticonstorm.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.client.codec.StringCodec
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig {

    @Bean
    fun redissonClient(
        @Value("\${redis.host}") redisHost: String,
        @Value("\${redis.port}") redisPort: String,
        @Value("\${redis.password}") redisPassword: String
    ): RedissonClient = Redisson.create(Config().also { config ->
        config.codec = StringCodec("UTF-8")
        config.useSingleServer().apply {
            address = "redis://${redisHost}:${redisPort}"
            password = redisPassword
            connectionMinimumIdleSize = 10
            connectionPoolSize = 30
            connectTimeout = 1000 * 5
            timeout = 1000 * 3
            idleConnectionTimeout = 1000 * 3
        }
    })
}
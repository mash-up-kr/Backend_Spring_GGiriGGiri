package mashup.ggiriggiri.gifticonstorm.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

const val APPLE_PUSH_URL = "appleurl.com"

@Configuration
class WebClientConfig {

    @Bean
    fun applePushWebClient(): WebClient {
        return WebClient.create(APPLE_PUSH_URL)
    }
}
package mashup.ggiriggiri.gifticonstorm.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI
import java.net.URL

const val APPLE_PUSH_URL = "appleurl.com"

@Configuration
class WebClientConfig(
    @Value("\${ncp.ocr.domain.ggiriggiri-general.url}")
    private val url : String,
    @Value("\${ncp.ocr.domain.ggiriggiri-general.secret}")
    private val secret : String,
) {

    @Bean
    fun applePushWebClient(): WebClient {
        return WebClient.create(APPLE_PUSH_URL)
    }

    @Bean
    fun naverCloudOCRClient(): WebClient {

        return WebClient.builder().apply {
            it.baseUrl(url)
            it.defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            it.defaultHeader("X-OCR-SECRET", secret)
            it.defaultHeader("Host", URI(url).host)
        }.build()
    }
}
package mashup.ggiriggiri.gifticonstorm.config

import mashup.ggiriggiri.gifticonstorm.common.setApplicationJsonContentType
import mashup.ggiriggiri.gifticonstorm.common.setHost
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

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
            it.setApplicationJsonContentType()
            it.setHost(url)
            it.defaultHeader("X-OCR-SECRET", secret)
        }.build()
    }

}
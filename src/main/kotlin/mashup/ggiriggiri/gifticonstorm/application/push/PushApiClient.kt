package mashup.ggiriggiri.gifticonstorm.application.push

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class PushApiClient(private val applePushWebClient: WebClient) {
    fun pushApi() {

    }
}
package mashup.ggiriggiri.gifticonstorm.application.push

import org.springframework.stereotype.Service

@Service
class PushService(private val pushApiClient: PushApiClient) {

    fun push() {
        pushApiClient.pushApi()
    }
}
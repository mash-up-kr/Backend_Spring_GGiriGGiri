package mashup.ggiriggiri.gifticonstorm.infrastructure

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient

@Component
class SigninBot(
    private val signinBotClient: WebClient,
) {

    companion object :Logger
    fun notify(memberCount: Long) {
        signinBotClient.post()
            .body(BodyInserters.fromFormData("content", "새로운 회원이 들어왔어요! [누적 회원 수: $memberCount]"))
            .retrieve()
            .bodyToMono(HashMap::class.java)
            .block()
            .also { log.info("[DiscordBot] success notify") }
    }

}
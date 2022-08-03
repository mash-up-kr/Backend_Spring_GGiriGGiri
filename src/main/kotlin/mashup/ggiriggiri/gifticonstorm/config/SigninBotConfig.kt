package mashup.ggiriggiri.gifticonstorm.config

import mashup.ggiriggiri.gifticonstorm.common.setApplicationJsonContentType
import mashup.ggiriggiri.gifticonstorm.common.setHost
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class SigninBotConfig(
    @Value("\${discord.signin.bot.token}")
    private val token : String,
    @Value("\${discord.base-url}")
    private val baseUrl : String,
    @Value("\${discord.signin.bot.channel.id}")
    private val channelId: String
) {

    @Bean
    fun signinBotClient(): WebClient {
        return WebClient.builder().apply {
            it.baseUrl("$baseUrl/channels/$channelId/messages")
            it.defaultHeader("Authorization", "Bot $token")
            it.setApplicationJsonContentType()
            it.setHost(baseUrl)
        }.build()
    }

}
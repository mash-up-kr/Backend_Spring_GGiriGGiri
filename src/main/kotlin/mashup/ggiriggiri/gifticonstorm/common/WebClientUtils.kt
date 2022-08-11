package mashup.ggiriggiri.gifticonstorm.common

import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import java.net.URI

fun WebClient.Builder.setApplicationJsonContentType() = this.defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
fun WebClient.Builder.setHost(url : String) = this.defaultHeader("Host", URI(url).host)
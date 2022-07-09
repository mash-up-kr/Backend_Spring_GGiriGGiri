package mashup.ggiriggiri.gifticonstorm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@SpringBootApplication
class GifticonStormApplication

fun main(args: Array<String>) {
	runApplication<GifticonStormApplication>(*args)
}

package mashup.ggiriggiri.gifticonstorm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import java.util.*

@EnableAsync
@SpringBootApplication
class GifticonStormApplication 

fun main(args: Array<String>) {
	TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
	runApplication<GifticonStormApplication>(*args)
}
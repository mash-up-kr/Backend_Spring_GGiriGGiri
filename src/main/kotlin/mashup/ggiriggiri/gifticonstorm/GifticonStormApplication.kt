package mashup.ggiriggiri.gifticonstorm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import java.util.*
import javax.annotation.PostConstruct

@EnableAsync
@SpringBootApplication
class GifticonStormApplication

fun main(args: Array<String>) {
	TimeZone.getTimeZone("Asia/Seoul")

	runApplication<GifticonStormApplication>(*args)

	@PostConstruct
	fun setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
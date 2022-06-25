package mashup.ggiriggiri.gifticonstorm.presentation

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class SampleController {
    @PostMapping("/restdocs/sample")
    fun sample(@RequestBody sampleRequestDto: SampleRequestDto): SampleResponseDto {
        return SampleResponseDto(357, "ice americano", "image.sample.com/image1")
    }
}

data class SampleRequestDto(
    val inherenceId: String
)
data class SampleResponseDto(
    val spreadId: Long,
    val merchandiseName: String,
    val imageUrl: String
)
package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.domain.dto.event.CreateEventRequestDto
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class BburigiController {

    companion object : Logger

    @PostMapping("/bburigi")
    fun createBburigi(
        @RequestPart(value = "image") image: MultipartFile,
        @RequestPart(value = "eventInfo") dto: CreateEventRequestDto
    ) : ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit)
    }
}

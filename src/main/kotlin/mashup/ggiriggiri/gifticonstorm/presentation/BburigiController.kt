package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.domain.dto.event.CreateEventRequestDto
import mashup.ggiriggiri.gifticonstorm.infrastructure.CouponImageUploader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class BburigiController {

    private val logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/bburigi")
    fun createBburigi(
        @RequestPart(value = "image") image: MultipartFile,
        @RequestPart(value = "eventInfo") dto: CreateEventRequestDto
    ) : ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit);
    }
}

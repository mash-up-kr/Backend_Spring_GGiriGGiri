package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.application.sprinkle.SprinkleService
import mashup.ggiriggiri.gifticonstorm.common.dto.BaseResponse
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.dto.event.CreateEventRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.GetSprinkleResDto
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
class SprinkleController(
    private val sprinkleService: SprinkleService
) {

    companion object : Logger

    @PostMapping("/sprinkle")
    fun createSprinkle(
        @RequestPart(value = "image") image: MultipartFile,
        @RequestPart(value = "eventInfo") dto: CreateEventRequestDto
    ) : ResponseEntity<Unit> {
        return ResponseEntity.ok(Unit)
    }

    @GetMapping("/api/v1/sprinkles")
    fun getSprinkles(
        @RequestParam(value = "orderBy", required = false) orderBy: OrderBy?,
        @RequestParam(value = "category", required = false) category: Category?,
        noOffsetRequest: NoOffsetRequest
    ): BaseResponse<List<GetSprinkleResDto>> {
        return BaseResponse.ok(
            sprinkleService.getSprinkles(orderBy, category, noOffsetRequest)
        )
    }
}

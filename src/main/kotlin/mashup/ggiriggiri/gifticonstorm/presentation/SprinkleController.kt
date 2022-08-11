package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.application.sprinkle.SprinkleService
import mashup.ggiriggiri.gifticonstorm.common.dto.BaseResponse
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.config.annotation.UserInfo
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.dto.event.CreateEventRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.CouponInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.DrawResultResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.GetSprinkleResDto
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import javax.validation.Valid
import kotlin.random.Random

@RestController
@RequestMapping("/api/v1")
class SprinkleController(
    private val sprinkleService: SprinkleService
) {

    companion object : Logger

    @PostMapping("/sprinkle")
    fun createSprinkle(
        @RequestPart(value = "image") image: MultipartFile,
        @RequestPart(value = "eventInfo") @Valid createEventRequestDto: CreateEventRequestDto,
        @UserInfo userInfoDto: UserInfoDto,
    ): BaseResponse<Unit> {
        sprinkleService.createSprinkle(image, createEventRequestDto, userInfoDto)
        return BaseResponse.ok()
    }

    @PostMapping("/sprinkle/{sprinkleId}/apply")
    fun applySprinkle(@UserInfo userInfoDto: UserInfoDto, @PathVariable sprinkleId: Long): BaseResponse<Unit> {
        sprinkleService.applySprinkle(userInfoDto, sprinkleId)
        return BaseResponse.ok()
    }

    @GetMapping("/sprinkles")
    fun getSprinkles(
        @UserInfo userInfoDto: UserInfoDto,
        @RequestParam(value = "orderBy", required = false) orderBy: OrderBy?,
        @RequestParam(value = "category", required = false) category: Category?,
        noOffsetRequest: NoOffsetRequest,
    ): BaseResponse<List<GetSprinkleResDto>> {
        return BaseResponse.ok(sprinkleService.getSprinkles(userInfoDto, orderBy, category, noOffsetRequest))
    }

    @GetMapping("/draw-result/{sprinkleId}")
    fun getDrawResult(@UserInfo userInfoDto: UserInfoDto, @PathVariable sprinkleId: Long): DrawResultResDto {
        return if (Random.nextBoolean())
            DrawResultResDto(DrawStatus.WIN, CouponInfoDto("베스킨라빈스", "싱글 레귤러", LocalDateTime.now().plusMonths(2), "http://dummyimage.com/240x100.png/dddddd/000000"))
        else
            DrawResultResDto(DrawStatus.LOSE)
    }
}

package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.application.CouponService
import mashup.ggiriggiri.gifticonstorm.common.dto.BaseResponse
import mashup.ggiriggiri.gifticonstorm.domain.coupon.dto.CouponSaveRequestDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
class CouponController(
    private val couponService: CouponService
) {

    @PostMapping("/api/v1/bburigi")
    fun saveCoupon(
        @RequestPart(value = "image") image: MultipartFile,
        @RequestPart(value = "couponInfo") @Valid requestDto: CouponSaveRequestDto
    ): ResponseEntity<BaseResponse<Unit>> {
        couponService.saveCoupon(image, requestDto)
        return ResponseEntity.ok(BaseResponse.ok())
    }
}
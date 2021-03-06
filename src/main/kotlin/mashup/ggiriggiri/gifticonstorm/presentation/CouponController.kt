package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.application.CouponService
import mashup.ggiriggiri.gifticonstorm.common.dto.BaseResponse
import mashup.ggiriggiri.gifticonstorm.domain.coupon.dto.CouponSaveRequestDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid

@RestController
class CouponController(
    private val couponService: CouponService
) {

    @PostMapping("/api/v1/sprinkle")
    fun saveCoupon(
        @RequestPart(value = "image") image: MultipartFile,
        @RequestPart(value = "couponInfo") @Valid requestDto: CouponSaveRequestDto
    ): BaseResponse<Unit> {
        return BaseResponse.ok()
    }
}
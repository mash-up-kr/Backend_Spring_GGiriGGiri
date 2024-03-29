package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.common.dto.BaseResponse
import mashup.ggiriggiri.gifticonstorm.config.annotation.UserInfo
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CouponController {

    @GetMapping("/api/v1/coupon/category")
    fun getCategory(@UserInfo userInfo: UserInfoDto): BaseResponse<Array<Category>> {
        return BaseResponse.ok(Category.categories)
    }
}
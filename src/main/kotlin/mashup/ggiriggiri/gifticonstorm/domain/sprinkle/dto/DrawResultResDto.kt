package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

import com.fasterxml.jackson.annotation.JsonInclude
import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DrawResultResDto(
    val drawStatus: DrawStatus,
    val couponInfo: CouponInfoDto? = null
) {
    companion object {
        fun of(drawStatus: DrawStatus, coupon: Coupon): DrawResultResDto {
            return if (drawStatus == DrawStatus.WIN)
                DrawResultResDto(drawStatus, CouponInfoDto.of(coupon))
            else
                DrawResultResDto(drawStatus)
        }
    }
}

data class CouponInfoDto(
    val brandName: String,
    val merchandiseName: String,
    val expiredAt: LocalDateTime,
    val imageUrl: String,
    val category: Category
) {
    companion object {
        fun of(coupon: Coupon): CouponInfoDto {
            return CouponInfoDto(
                brandName = coupon.brandName,
                merchandiseName = coupon.merchandiseName,
                expiredAt = coupon.expiredAt,
                imageUrl = coupon.imageUrl,
                coupon.category
            )
        }
    }
}

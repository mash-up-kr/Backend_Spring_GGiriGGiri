package mashup.ggiriggiri.gifticonstorm.common.validator

import mashup.ggiriggiri.gifticonstorm.common.annotation.CouponTimeCheck
import mashup.ggiriggiri.gifticonstorm.domain.coupon.dto.CouponSaveRequestDto
import org.springframework.util.StringUtils
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.regex.Pattern
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CouponTimeCheckValidator: ConstraintValidator<CouponTimeCheck, CouponSaveRequestDto> {

    override fun isValid(value: CouponSaveRequestDto, context: ConstraintValidatorContext): Boolean {
        if (!StringUtils.hasText(value.couponExpiredTime)) {
            return false
        }

        val pattern = Pattern.compile("^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$") //YYYY-MM-DD
        if (!pattern.matcher(value.couponExpiredTime).matches()) {
            addConstraintViolation(context, "쿠폰 유효기간 형식(YYYY-MM-DD)을 확인해주세요.", "couponExpiredTime")
            return false
        }

        val couponExpiredTime = LocalDate.parse(value.couponExpiredTime).atTime(LocalTime.MAX)
        if (couponExpiredTime.isBefore(LocalDateTime.now())) {
            addConstraintViolation(context, "쿠폰 유효기간이 지났습니다.", "couponExpiredTime")
            return false
        }

        if (value.bburigiTime !in 1..24) {
            return false
        }

        val bburigiTime = LocalDateTime.now().plusHours(value.bburigiTime)
        if (bburigiTime.isAfter(couponExpiredTime)) {
            addConstraintViolation(context, "뿌리기 시간은 쿠폰 유효기간 이내여야 합니다.", "bburigiTime")
            return false
        }

        return true
    }

    private fun addConstraintViolation(context: ConstraintValidatorContext, errorMessage: String, node: String) {
        context.disableDefaultConstraintViolation()
        context.buildConstraintViolationWithTemplate(errorMessage)
            .addPropertyNode(node)
            .addConstraintViolation()
    }

}
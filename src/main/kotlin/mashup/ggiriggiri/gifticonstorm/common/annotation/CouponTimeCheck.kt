package mashup.ggiriggiri.gifticonstorm.common.annotation

import mashup.ggiriggiri.gifticonstorm.common.validator.CouponTimeCheckValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [CouponTimeCheckValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class CouponTimeCheck(
    val message: String = "invalid coupon time default message",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

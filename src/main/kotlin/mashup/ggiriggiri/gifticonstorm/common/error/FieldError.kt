package mashup.ggiriggiri.gifticonstorm.common.error

import org.springframework.validation.BindingResult

class FieldError(
    val field: String,
    val message: String?
) {

    companion object {
        fun of(bindingResult: BindingResult): List<FieldError> {
            val fieldErrors = bindingResult.fieldErrors
            return fieldErrors.map { error ->
                FieldError(
                    field = error.field,
                    message = error.defaultMessage
                )
            }.toList()
        }
    }
}
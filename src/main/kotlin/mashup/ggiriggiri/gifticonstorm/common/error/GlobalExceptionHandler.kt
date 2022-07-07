package mashup.ggiriggiri.gifticonstorm.common.error

import mashup.ggiriggiri.gifticonstorm.common.dto.BaseResponse
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): BaseResponse<List<FieldError>> {
        val fieldErrors = FieldError.of(exception.bindingResult)
        return BaseResponse.error(ErrorCode.INVALID_INPUT_VALUE, fieldErrors)
    }

    @ExceptionHandler(BaseException::class)
    private fun handleBaseException(exception: BaseException): ResponseEntity<BaseResponse<Unit>> {
        return ResponseEntity.status(exception.errorCode.status)
            .body(BaseResponse.error(exception.errorCode))
    }
}
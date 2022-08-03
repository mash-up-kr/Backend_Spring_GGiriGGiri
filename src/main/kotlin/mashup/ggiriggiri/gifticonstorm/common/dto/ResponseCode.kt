package mashup.ggiriggiri.gifticonstorm.common.dto

import org.springframework.http.HttpStatus

enum class ResponseCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {
    //Success
    OK(HttpStatus.OK, "S001", "Success"),

    //Fail
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "F001", "No Header Name: Authorization"),
    FAILED_RECOGNIZE(HttpStatus.INTERNAL_SERVER_ERROR, "F002", "FAILED_RECOGNIZE_IMAGE"),
    NOT_SUPPORT_OCR_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "F003", "NOT_SUPPORTED_IMAGE_FORMAT"),

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid Input Value"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "Internal Server Error")
}

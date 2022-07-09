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
    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "Invalid Input Value"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "Internal Server Error")
}
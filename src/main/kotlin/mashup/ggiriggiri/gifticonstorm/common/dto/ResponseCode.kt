package mashup.ggiriggiri.gifticonstorm.common.dto

import org.springframework.http.HttpStatus

enum class ResponseCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {
    //Success
    OK(HttpStatus.OK, "S001", "성공했습니다."),

    //Fail
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "F001", "사용자 인증에 실패했습니다."),
    FAILED_RECOGNIZE(HttpStatus.INTERNAL_SERVER_ERROR, "F002", "OCR에 실패했습니다."),
    NOT_SUPPORT_OCR_IMAGE_FORMAT(HttpStatus.BAD_REQUEST, "F003", "지원하지 않는 이미지 형식입니다."),

    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "F004", "해당 데이터를 찾을 수 없습니다."),

    INVALID_PARTICIPATE_REQUEST(HttpStatus.BAD_REQUEST, "F005", "내가 등록한 기프티콘에는 응모할 수 없어요!"),
    ALREADY_PARTICIPATE_IN(HttpStatus.BAD_REQUEST, "F006", "이미 응모한 뿌리기 입니다."),
    ALREADY_EXPIRED_SPRINKLE(HttpStatus.BAD_REQUEST, "F007", "이미 만료된 뿌리기 입니다."),

    PAYLOAD_TOO_LARGE(HttpStatus.PAYLOAD_TOO_LARGE, "F008", "HTTP PAYLOAD가 너무 큽니다."),

    // Common
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C001", "유효하지 않은 입력값 입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C002", "서버 에러가 발생했습니다.")
}

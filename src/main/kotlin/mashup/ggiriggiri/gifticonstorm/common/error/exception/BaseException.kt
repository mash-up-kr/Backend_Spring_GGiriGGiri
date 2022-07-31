package mashup.ggiriggiri.gifticonstorm.common.error.exception

import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode

open class BaseException(
    val responseCode: ResponseCode,
    val errorMessage: String
): RuntimeException(errorMessage) {
    constructor(responseCode: ResponseCode) : this(responseCode, responseCode.message)
}
package mashup.ggiriggiri.gifticonstorm.common.error.exception

import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode

open class BaseException(
    val responseCode: ResponseCode,
    override val message: String
): RuntimeException(message) {

    constructor(responseCode: ResponseCode) : this(responseCode, responseCode.message)
}
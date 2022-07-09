package mashup.ggiriggiri.gifticonstorm.common.error.exception

import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode

class BaseException(
    val responseCode: ResponseCode
): RuntimeException()
package mashup.ggiriggiri.gifticonstorm.common.error.exception

import mashup.ggiriggiri.gifticonstorm.common.error.ErrorCode

class BaseException(
    val errorCode: ErrorCode
): RuntimeException()
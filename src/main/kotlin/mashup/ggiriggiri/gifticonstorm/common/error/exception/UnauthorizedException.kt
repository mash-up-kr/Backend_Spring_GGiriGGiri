package mashup.ggiriggiri.gifticonstorm.common.error.exception

import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode

class UnauthorizedException(
    override val message: String
) : BaseException(ResponseCode.UNAUTHORIZED, message)
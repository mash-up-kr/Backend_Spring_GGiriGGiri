package mashup.ggiriggiri.gifticonstorm.common.error.exception

import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode

data class SprinkleNotFoundException(
    override val message: String
) : BaseException(ResponseCode.DATA_NOT_FOUND, message)
package mashup.ggiriggiri.gifticonstorm.common.error.exception

import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode

data class EntityNotFoundException(
    val target: String,
    val errorMessage: String
) : BaseException(ResponseCode.DATA_NOT_FOUND, "$target Not Found -> $errorMessage")
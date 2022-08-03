package mashup.ggiriggiri.gifticonstorm.common.error.exception

import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode

class OcrFailedException : BaseException(ResponseCode.FAILED_RECOGNIZE)
package mashup.ggiriggiri.gifticonstorm.infrastructure

import mashup.ggiriggiri.gifticonstorm.common.error.StringUtils

data class OcrResultResponseDto(
    private val _expiredAt: String,
    private val _brandName: String,
    private val _merchandiseName: String,
) {
    val expiredAt = StringUtils.removeNewLine(_expiredAt)
    val brandName = StringUtils.removeNewLine(_brandName)
    val merchandiseName = StringUtils.removeNewLine(_merchandiseName)
}

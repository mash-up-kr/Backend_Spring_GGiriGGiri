package mashup.ggiriggiri.gifticonstorm.infrastructure

import mashup.ggiriggiri.gifticonstorm.common.removeBlank
import mashup.ggiriggiri.gifticonstorm.common.removeNewLine
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class OcrResultResponseDto(
    private val _expiredAt: OcrResultDateTime,
    private val _brandName: String,
    private val _merchandiseName: String,
) {
    val brandName = _brandName.removeNewLine()
    val merchandiseName = _merchandiseName.removeNewLine()
    val expiredAt = _expiredAt.dateTime
}

interface OcrResultDateTime {
    val formatter : DateTimeFormatter
    val dateTime: LocalDateTime
    fun preprocess(s: String) : String
}

data class KakaoLocalDateTime(
    private val s : String,
) : OcrResultDateTime {

    override val formatter = DateTimeFormatter.ofPattern("yyyy년MM월dd일")
    override val dateTime = LocalDate.parse(preprocess(s), formatter).atTime(23, 59, 59)

    override fun preprocess(s: String): String {
        return s.removeNewLine()
            .removeBlank()
    }
}

data class GifticonLocalDateTime(
    private val s : String,
) : OcrResultDateTime {

    override val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")
    override val dateTime = LocalDate.parse(preprocess(s), formatter).atTime(23, 59, 59)

    override fun preprocess(s: String): String {
        return s.removeNewLine()
            .removeBlank()
    }
}
package mashup.ggiriggiri.gifticonstorm.infrastructure

import com.fasterxml.jackson.annotation.JsonProperty

// TODO: 카카오 쿠폰 등등 종류별로 사용할 수 있도록 객체 관계 설계

interface OcrResult {
    fun isSuccess(): Boolean
    fun toDto(): OcrResultResponseDto
}

data class NaverOcrResult(
    val images: List<Images>,
) : OcrResult {

    override fun isSuccess(): Boolean {
        return images[0].isSuccess()
    }

    override fun toDto(): OcrResultResponseDto {
        val recognizedTime = images[0].fields.first { it.name == "expiredAt" }.inferText

        val dateTime = getDateTimeVo(recognizedTime)

        return OcrResultResponseDto(
            _brandName = images[0].fields.first { it.name == "brandName" }.inferText,
            _merchandiseName = images[0].fields.first { it.name == "merchandiseName" }.inferText,
            _expiredAt = dateTime
        )
    }

    private fun getDateTimeVo(recognizedTime: String) = when (images[0].matchedTemplate.ocrTemplate) {
        OcrTemplate.kakao -> KakaoLocalDateTime(recognizedTime)
        OcrTemplate.gifticon -> GifticonLocalDateTime(recognizedTime)
    }

}


data class Images(
    val inferResult: InferResult,
    val matchedTemplate: MatchedTemplate,
    val fields: List<Field>
) {

    fun isSuccess(): Boolean {
        return inferResult == InferResult.SUCCESS
    }
}

enum class InferResult {
    SUCCESS, ERROR, FAILURE;
}

data class Field(
    val name: String,
    val inferText: String,
)

data class MatchedTemplate(
    @JsonProperty("name")
    val ocrTemplate: OcrTemplate
)

enum class OcrTemplate {
    kakao, gifticon;
}
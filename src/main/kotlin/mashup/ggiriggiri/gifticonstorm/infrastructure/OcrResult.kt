package mashup.ggiriggiri.gifticonstorm.infrastructure

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

    override fun toDto() = OcrResultResponseDto(
        _brandName = images[0].fields.first { it.name == "brandName" }.inferText,
        _merchandiseName = images[0].fields.first { it.name == "merchandiseName" }.inferText,
        _expiredAt = images[0].fields.first { it.name == "expiredAt" }.inferText
    )

}

data class Images(
    val inferResult: InferResult,
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

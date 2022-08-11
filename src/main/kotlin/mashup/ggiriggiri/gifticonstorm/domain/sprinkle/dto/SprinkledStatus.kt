package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto

enum class SprinkledStatus(
    private val description: String
) {

    PROGRESS("응모 진행중"),
    FINISH("전달완료"),
    NO_PARTICIPANTS("받은 사람 없음")
}
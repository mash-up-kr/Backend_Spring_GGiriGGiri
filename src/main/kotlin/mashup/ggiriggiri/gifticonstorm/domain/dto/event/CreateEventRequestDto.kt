package mashup.ggiriggiri.gifticonstorm.domain.dto.event

data class CreateEventRequestDto(
    val category: String,
    val brandName: String,
    val merchandiseName: String,
    val couponExpiredTime: String,
    val sprinkleTime: String,
)

package mashup.ggiriggiri.gifticonstorm.common.dto

data class NoOffsetRequest(
    val id: Long?,
    val limit: Long = maxLimit
) {

    companion object {
        private const val maxLimit = 10L

        fun of(): NoOffsetRequest {
            return NoOffsetRequest(id = null, limit = maxLimit)
        }

        fun of(id: Long?, limit: Long): NoOffsetRequest {
            return NoOffsetRequest(id = id, limit = if(limit > maxLimit) maxLimit else limit)
        }
    }
}

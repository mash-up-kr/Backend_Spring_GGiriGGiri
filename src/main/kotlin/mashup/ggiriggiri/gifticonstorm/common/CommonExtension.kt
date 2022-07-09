package mashup.ggiriggiri.gifticonstorm.common

fun Any.toJson(): String {
    return DEFAULT_OBJECT_MAPPER.writeValueAsString(this)
}
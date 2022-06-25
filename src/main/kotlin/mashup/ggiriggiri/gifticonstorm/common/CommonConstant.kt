package mashup.ggiriggiri.gifticonstorm.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

val DEFAULT_OBJECT_MAPPER = Jackson2ObjectMapperBuilder()
    .failOnEmptyBeans(false)
    .failOnUnknownProperties(false)
    .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
    .modules(JavaTimeModule(), KotlinModule.Builder()
        .withReflectionCacheSize(512)
        .configure(KotlinFeature.NullToEmptyCollection, false)
        .configure(KotlinFeature.NullToEmptyMap, false)
        .configure(KotlinFeature.NullIsSameAsDefault, false)
        .configure(KotlinFeature.SingletonSupport, false)
        .configure(KotlinFeature.StrictNullChecks, false)
        .build()
    )
    .build<ObjectMapper>()
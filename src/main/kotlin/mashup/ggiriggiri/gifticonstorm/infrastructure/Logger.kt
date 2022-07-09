package mashup.ggiriggiri.gifticonstorm.infrastructure

import org.slf4j.LoggerFactory

interface Logger {
    val log get() = LoggerFactory.getLogger(this::class.java)
}
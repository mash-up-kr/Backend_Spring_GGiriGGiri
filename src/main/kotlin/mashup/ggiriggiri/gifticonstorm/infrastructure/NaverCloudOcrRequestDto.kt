package mashup.ggiriggiri.gifticonstorm.infrastructure

import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.coyote.http11.Constants.a
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime
import java.util.Base64
import java.util.UUID

class NaverCloudOcrRequestDto(
    fileName : FileName,
    base64String: String,
) {
    val version = "V2"
    val requestId = UUID.randomUUID()
    val timestamp = System.currentTimeMillis()
    val lang = "ko"
    val images = listOf(Image(name = fileName.getName(), format = ImageFormat.get(fileName.getExtension()).name.lowercase(), data = base64String))
    val enableTableDetection = false

    fun isSupported(): Boolean {
        return images.first().format != ImageFormat.NOT_SUPPORT.name
    }
}

data class FileName(private val fileName: String) {

    private val name: String
    private val extension: String

    init {
        val splited = fileName.split(".")
        name = splited[0]
        extension = splited[1]
    }

    fun getName(): String {
        return name
    }

    fun getExtension(): String {
        return extension
    }
}

enum class ImageFormat(
    private val format: String,
) {
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    PDF("pdf"),
    TIFF("tiff"),
    NOT_SUPPORT("NULL");

    companion object Factory {
        private val map = ImageFormat.values().associateBy { it.format }

        fun get(format : String): ImageFormat {
            return map[format.lowercase()] ?: NOT_SUPPORT
        }
    }
}

data class Image(
    val format: String,
    val name: String,
    val data: String
)
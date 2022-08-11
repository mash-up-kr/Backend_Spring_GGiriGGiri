package mashup.ggiriggiri.gifticonstorm.common

import java.io.InputStream
import java.util.*

class Base64Utils {

    companion object {
        fun toBase64String(file: InputStream): String {
            val fileContent: ByteArray = file.readBytes()
            return Base64.getEncoder()
                .encodeToString(fileContent)
        }
    }

}

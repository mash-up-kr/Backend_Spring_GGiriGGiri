package mashup.ggiriggiri.gifticonstorm.infrastructure

import org.springframework.web.multipart.MultipartFile

interface ImageUploader {
    fun upload(file: MultipartFile)
}

package mashup.ggiriggiri.gifticonstorm.infrastructure

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.ObjectMetadata
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@Component
class S3ImageUploader(
    private val amazonS3Client : AmazonS3Client,

    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String
) : ImageUploader {

    companion object : Logger

    override fun upload(file: MultipartFile) {
        upload(file, generateKey(file))
    }

    private fun upload(file: MultipartFile, key: String) {
        val result = amazonS3Client.putObject(bucket, key, file.inputStream, getObjectMetadata(file.inputStream.available().toLong()))
        log.info("image uploaded path: $bucket/$key")
    }

    private fun getObjectMetadata(contentLength: Long) : ObjectMetadata {
        return ObjectMetadata().apply {
            this.contentLength = contentLength
        }
    }

    private fun generateKey(file: MultipartFile): String {
        return "${LocalDateTime.now()}-${file.originalFilename}"
    }

}

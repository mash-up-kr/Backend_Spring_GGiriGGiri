package mashup.ggiriggiri.gifticonstorm.infrastructure

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime
import java.util.*

@Component
class S3ImageUploader(
    private val amazonS3Client : AmazonS3Client,

    @Value("\${cloud.aws.s3.bucket}")
    private val bucket: String
) : ImageUploader {

    companion object : Logger

    override fun upload(file: MultipartFile): String {
        return upload(file, generateKey(file))
    }

    private fun upload(file: MultipartFile, key: String): String {
        amazonS3Client.putObject(bucket, key, file.inputStream, getObjectMetadata(file.inputStream.available().toLong()))
        log.info("image uploaded path: $bucket/$key")
        return amazonS3Client.getUrl(bucket, key).toString()
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

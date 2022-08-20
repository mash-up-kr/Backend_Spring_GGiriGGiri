package mashup.ggiriggiri.gifticonstorm.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import mashup.ggiriggiri.gifticonstorm.common.Base64Utils
import mashup.ggiriggiri.gifticonstorm.common.error.exception.NotSupportedOcrImageType
import mashup.ggiriggiri.gifticonstorm.common.error.exception.OcrFailedException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.client.WebClient

@Service
class NaverCloudOcrService(
    private val naverCloudOCRClient: WebClient,
    private val objectMapper: ObjectMapper,
    private val s3ImageUploader: S3ImageUploader,
) : OCRService {

    companion object : Logger

    override fun recognize(file: MultipartFile): OcrResult {

        try {
            val bodyJson = getRequestBody(file)
            return request(bodyJson)
        } catch (e: Exception) {
            s3ImageUploader.upload(file)

            when(e) {
                is NotSupportedOcrImageType -> { throw e }
                else -> throw OcrFailedException()
            }
        }
        
    }

    private fun getRequestBody(file: MultipartFile): String {
        val body = NaverCloudOcrRequestDto(
            FileName(fileName = file.originalFilename!!),
            Base64Utils.toBase64String(file.inputStream)
        )

        if (!body.isSupported()) {
            log.error("[OCR] NOT SUPPORTED IMAGE FORMAT fileName: ${file.originalFilename}")
            throw NotSupportedOcrImageType()
        }

        return objectMapper.writeValueAsString(body)!!
    }

    private fun request(body: String) : NaverOcrResult {
        val result = requestInternal(body)

        if (result.contains("code")) {
            log.error("OCR FAIL : $result")
            throw OcrFailedException()
        }

        val ocrResult = objectMapper.convertValue(result, NaverOcrResult::class.java)

        if (!ocrResult.isSuccess()) {
            log.error("OCR FAIL : $result")
            throw OcrFailedException()
        }

        return objectMapper.convertValue(result, NaverOcrResult::class.java).also {
            log.info("OCR SUCCESS : $it")
        }
    }

    private fun requestInternal(body: String) = naverCloudOCRClient.post()
        .bodyValue(body)
        .header("Content-Length", getContentLength(body))
        .retrieve()
        .bodyToMono(HashMap::class.java)
        .block()!!

    private fun getContentLength(bodyJson: String) = bodyJson.toByteArray().size.toString()

}

package mashup.ggiriggiri.gifticonstorm.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import mashup.ggiriggiri.gifticonstorm.common.Base64Utils
import mashup.ggiriggiri.gifticonstorm.common.error.exception.NotSupportedOcrImageType
import mashup.ggiriggiri.gifticonstorm.common.error.exception.OcrFailedException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.io.File

@Service
class NaverCloudOcrService(
    private val naverCloudOCRClient: WebClient,
    private val objectMapper: ObjectMapper,
) : OCRService {

    companion object : Logger

    override fun recognize(file: MultipartFile): OcrResult {
        val bodyJson = getRequestBody(file)
        return request(bodyJson)
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
        val result = naverCloudOCRClient.post()
            .bodyValue(body)
            .header("Content-Length", getContentLength(body))
            .retrieve()
            .bodyToMono(HashMap::class.java)
            .block()!!

        if (result.contains("code")) {
            log.error("OCR FAIL : $result")
            throw OcrFailedException()
        }

        val ocrResult = objectMapper.convertValue(result, NaverOcrResult::class.java)

        if (!ocrResult.isSuccess()) {
            log.error("OCR FAIL : $result")
            throw OcrFailedException()
        }

        return objectMapper.convertValue(result, NaverOcrResult::class.java)
    }

    private fun getContentLength(bodyJson: String) = bodyJson.toByteArray().size.toString()

}

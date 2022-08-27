package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.common.dto.BaseResponse
import mashup.ggiriggiri.gifticonstorm.config.annotation.UserInfo
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.infrastructure.NaverCloudOcrService
import mashup.ggiriggiri.gifticonstorm.infrastructure.OcrResultResponseDto
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RequestMapping("/ocr")
@RestController
class OcrController(private val naverOcrService: NaverCloudOcrService) {

    @PostMapping
    fun ocr(
        @UserInfo userInfo: UserInfoDto,
        @RequestPart(value = "image") image: MultipartFile,
    ): BaseResponse<OcrResultResponseDto> {
        val ocrResult = naverOcrService.recognize(image)
        return BaseResponse.ok(ocrResult.toDto())
    }
}

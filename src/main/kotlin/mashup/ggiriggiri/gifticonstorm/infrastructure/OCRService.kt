package mashup.ggiriggiri.gifticonstorm.infrastructure

import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
interface OCRService  {

    fun recognize(file: MultipartFile): OcrResult

}

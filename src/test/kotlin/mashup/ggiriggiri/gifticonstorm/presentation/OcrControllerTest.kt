package mashup.ggiriggiri.gifticonstorm.presentation

import com.ninjasquad.springmockk.MockkBean
import io.kotest.mpp.file
import io.mockk.every
import io.mockk.justRun
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.infrastructure.*
import mashup.ggiriggiri.gifticonstorm.presentation.restdocs.TestRestDocs
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(OcrController::class)
internal class OcrControllerTest : TestRestDocs() {

    @MockkBean
    private lateinit var ocrService: NaverCloudOcrService

    @MockkBean
    private lateinit var memberRepository: MemberRepository

    @MockkBean
    private lateinit var signinBot: SigninBot

    @Test
    fun `OCR 성공`() {
        //given
        val image = MockMultipartFile(
            "image",
            "file.png",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "<<image data>>".toByteArray()
        )

        val result = given()

        every { ocrService.recognize(image) } returns result

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/ocr")
                .file(image)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "OCR 요청",
                    HeaderDocumentation.requestHeaders(),
                    RequestDocumentation.requestParts(
                        RequestDocumentation.partWithName("image").description("기프티콘 이미지"),
                    ),
                    PayloadDocumentation.requestPartBody("image"),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                        PayloadDocumentation.fieldWithPath("data.brandName").type(JsonFieldType.STRING).description("제공처"),
                        PayloadDocumentation.fieldWithPath("data.merchandiseName").type(JsonFieldType.STRING).description("상품명"),
                        PayloadDocumentation.fieldWithPath("data.expiredAt").type(JsonFieldType.STRING).description("유효기간")
                    ),
                    HeaderDocumentation.responseHeaders()
                )
            )
    }

    private fun given() = NaverOcrResult(
        listOf(
            Images(
                inferResult = InferResult.SUCCESS,
                matchedTemplate = MatchedTemplate(OcrTemplate.kakao),
                fields = listOf(
                    Field("expiredAt", "2022년12월10일"),
                    Field("brandName", "스타벅스"),
                    Field("merchandiseName", "아메리카."),
                ),
            )
        )
    )


}


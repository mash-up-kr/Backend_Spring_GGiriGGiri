package mashup.ggiriggiri.gifticonstorm.presentation

import com.ninjasquad.springmockk.MockkBean
import mashup.ggiriggiri.gifticonstorm.common.DEFAULT_OBJECT_MAPPER
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.infrastructure.SigninBot
import mashup.ggiriggiri.gifticonstorm.presentation.restdocs.TestRestDocs
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers


@WebMvcTest(SampleController::class)
internal class SampleControllerTest : TestRestDocs() {

    @MockkBean
    private lateinit var memberRepository: MemberRepository

    @MockkBean
    private lateinit var signinBot: SigninBot

    @Test
    fun sample() {
        val sampleRequestDto = SampleRequestDto("3a78Bds78")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/restdocs/sample")
                .content(DEFAULT_OBJECT_MAPPER.writeValueAsString(sampleRequestDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print()).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andDo(
            MockMvcRestDocumentation.document(
                "sample/{methodName}",
                HeaderDocumentation.requestHeaders(),
                PayloadDocumentation.requestFields(
                    PayloadDocumentation.fieldWithPath("inherenceId").type(JsonFieldType.STRING).description("고유 id"),
                ),
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("spreadId").type(JsonFieldType.NUMBER).description("뿌리기 id"),
                    PayloadDocumentation.fieldWithPath("merchandiseName").type(JsonFieldType.STRING).description("제품명"),
                    PayloadDocumentation.fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 url"),
                ),
                HeaderDocumentation.responseHeaders()
            )
        )

    }
}
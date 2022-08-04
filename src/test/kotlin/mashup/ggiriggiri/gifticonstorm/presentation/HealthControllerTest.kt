package mashup.ggiriggiri.gifticonstorm.presentation

import com.ninjasquad.springmockk.MockkBean
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.infrastructure.SigninBot
import mashup.ggiriggiri.gifticonstorm.presentation.restdocs.TestRestDocs
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(HealthController::class)
internal class HealthControllerTest : TestRestDocs() {

    @MockkBean
    private lateinit var memberRepository: MemberRepository

    @MockkBean
    private lateinit var signinBot: SigninBot

    @Test
    fun health() {
        mockMvc.perform(
            MockMvcRequestBuilders.get("/health")
        ).andDo(MockMvcResultHandlers.print()).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andDo(
            MockMvcRestDocumentation.document(
                "health/{methodName}"
            )
        )
    }
}
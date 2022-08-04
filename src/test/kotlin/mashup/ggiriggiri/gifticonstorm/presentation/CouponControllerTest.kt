package mashup.ggiriggiri.gifticonstorm.presentation

import com.ninjasquad.springmockk.MockkBean
import mashup.ggiriggiri.gifticonstorm.application.CouponService
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.infrastructure.SigninBot
import mashup.ggiriggiri.gifticonstorm.presentation.restdocs.TestRestDocs
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(CouponController::class)
internal class CouponControllerTest : TestRestDocs() {

    @MockkBean
    private lateinit var couponService: CouponService

    @MockkBean
    private lateinit var memberRepository: MemberRepository

    @MockkBean
    private lateinit var signinBot: SigninBot

    @Test
    fun `쿠폰 카테고리 목록 조회 성공`() {
        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/coupon/category")
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "쿠폰 카테고리 목록 조회/{methodName}",
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                        PayloadDocumentation.fieldWithPath("data").type(JsonFieldType.ARRAY).description("카테고리 목록"),
                    )
                )
            )
    }
}
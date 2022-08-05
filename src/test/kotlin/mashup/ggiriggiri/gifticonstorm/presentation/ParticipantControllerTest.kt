package mashup.ggiriggiri.gifticonstorm.presentation

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import mashup.ggiriggiri.gifticonstorm.application.participant.ParticipantService
import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.domain.participant.dto.ParticipantInfoResDto
import mashup.ggiriggiri.gifticonstorm.infrastructure.SigninBot
import mashup.ggiriggiri.gifticonstorm.presentation.restdocs.TestRestDocs
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.LocalDate

@WebMvcTest(ParticipantController::class)
internal class ParticipantControllerTest : TestRestDocs() {

    @MockkBean
    private lateinit var signinBot: SigninBot

    @MockkBean
    private lateinit var memberRepository: MemberRepository

    private val userInfoDto = UserInfoDto(id = 0, inherenceId = "test-user")

    @BeforeEach
    fun setUp() {
        every { memberRepository.findByInherenceId(userInfoDto.inherenceId) } returns Member(inherenceId = userInfoDto.inherenceId)
    }

    @MockkBean
    private lateinit var participantService: ParticipantService

    private val now = LocalDate.now()

    @Test
    fun `내 응모 내역 조회 성공`() {
        //given
        val noOffsetRequest = NoOffsetRequest.of(1, 10)

        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("id", noOffsetRequest.id?.toString())
        requestParams.add("limit", noOffsetRequest.limit.toString())

        val resDto = ParticipantInfoResDto(
            participantId = 1,
            sprinkleId = 1,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            expiredAt = now.toString(),
            category = Category.CAFE,
            participants = 100,
            participateDate = now.minusDays(1).toString(),
            isChecked = false,
            drawStatus = DrawStatus.WIN
        )
        val resultData = listOf(resDto)

        every { participantService.getParticipantHistory(userInfoDto, noOffsetRequest) } returns resultData

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/participant/history")
                .header("Authorization", userInfoDto.inherenceId)
                .queryParams(requestParams)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.code").value(ResponseCode.OK.code)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message").value(ResponseCode.OK.message)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].participantId").value(1)
            )
            .andDo(
                MockMvcRestDocumentation.document(
                    "응모 내역 조회/{methodName}",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("애플 사용자 고유 id"),
                    ),
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("id").description("마지막으로 전달받은 뿌리기 id (첫 요청시 id = null)"),
                        RequestDocumentation.parameterWithName("limit").description("조회 개수"),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                        PayloadDocumentation.fieldWithPath("data[0].participantId").type(JsonFieldType.NUMBER).description("응모 id"),
                        PayloadDocumentation.fieldWithPath("data[0].sprinkleId").type(JsonFieldType.NUMBER).description("뿌리기 id"),
                        PayloadDocumentation.fieldWithPath("data[0].brandName").type(JsonFieldType.STRING).description("브랜드명"),
                        PayloadDocumentation.fieldWithPath("data[0].merchandiseName").type(JsonFieldType.STRING).description("상품명"),
                        PayloadDocumentation.fieldWithPath("data[0].expiredAt").type(JsonFieldType.STRING).description("유효기간"),
                        PayloadDocumentation.fieldWithPath("data[0].category").type(JsonFieldType.STRING).description("카테고리"),
                        PayloadDocumentation.fieldWithPath("data[0].participants").type(JsonFieldType.NUMBER).description("참여자 수"),
                        PayloadDocumentation.fieldWithPath("data[0].participateDate").type(JsonFieldType.STRING).description("응모 날짜"),
                        PayloadDocumentation.fieldWithPath("data[0].isChecked").type(JsonFieldType.BOOLEAN).description("응모 결과 확인 여부"),
                        PayloadDocumentation.fieldWithPath("data[0].drawStatus").type(JsonFieldType.STRING).description("응모 진행 상태"),
                    )
                )
            )
    }
}
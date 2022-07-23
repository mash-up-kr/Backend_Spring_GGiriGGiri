package mashup.ggiriggiri.gifticonstorm.presentation

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import mashup.ggiriggiri.gifticonstorm.application.sprinkle.SprinkleService
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.GetSprinkleResDto
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
import java.time.LocalDateTime

@WebMvcTest(SprinkleController::class)
internal class SprinkleControllerGetSprinkleTest : TestRestDocs() {

    @MockkBean
    private lateinit var sprinkleService: SprinkleService

    @MockkBean
    private lateinit var memberRepository: MemberRepository

    private val userInfoDto = UserInfoDto(id = 0, inherenceId = "test-user")

    @BeforeEach
    fun setUp() {
        every { memberRepository.findByInherenceId(userInfoDto.inherenceId) } returns Member(inherenceId = userInfoDto.inherenceId)
    }

    @Test
    fun `뿌리기 마감임박 조회 성공`() {
        //given
        val orderBy: OrderBy = OrderBy.DEADLINE
        val category: Category = Category.ALL

        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("orderBy", orderBy.toString())
        requestParams.add("category", category.toString())

        val resultDto = GetSprinkleResDto(
            sprinkleId = 1,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            category = Category.CAFE,
            expiredAt = LocalDate.now().plusDays(1).toString(),
            sprinkleAt = LocalDateTime.now().plusMinutes(10).toString(),
            participants = 100,
            participateIn = true
        )
        val resultData = listOf(resultDto)
        every { sprinkleService.getSprinkles(userInfoDto, orderBy, category, NoOffsetRequest.of()) } returns resultData

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/sprinkles")
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
                MockMvcResultMatchers.jsonPath("\$.data.size()").value(1)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].brandName").value("스타벅스")
            )
            .andDo(
                MockMvcRestDocumentation.document(
                    "뿌리기조회/{methodName}",
                    HeaderDocumentation.requestHeaders(),
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("orderBy").description("정렬 조건"),
                        RequestDocumentation.parameterWithName("category").description("카테고리 종류")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                        PayloadDocumentation.fieldWithPath("data[0].sprinkleId").type(JsonFieldType.NUMBER).description("뿌리기 id"),
                        PayloadDocumentation.fieldWithPath("data[0].brandName").type(JsonFieldType.STRING).description("브랜드명"),
                        PayloadDocumentation.fieldWithPath("data[0].merchandiseName").type(JsonFieldType.STRING).description("상품명"),
                        PayloadDocumentation.fieldWithPath("data[0].category").type(JsonFieldType.STRING).description("카테고리"),
                        PayloadDocumentation.fieldWithPath("data[0].expiredAt").type(JsonFieldType.STRING).description("유효기간"),
                        PayloadDocumentation.fieldWithPath("data[0].sprinkleAt").type(JsonFieldType.STRING).description("뿌리기 시간"),
                        PayloadDocumentation.fieldWithPath("data[0].participants").type(JsonFieldType.NUMBER).description("응모자 수"),
                        PayloadDocumentation.fieldWithPath("data[0].participateIn").type(JsonFieldType.BOOLEAN).description("해당 사용자 응모 여부")
                    ),
                    HeaderDocumentation.responseHeaders()
                )
            )
    }

    @Test
    fun `뿌리기 전체 조회 성공`() {
        //given
        val orderBy = OrderBy.CREATED_AT
        val category = Category.ALL
        val noOffsetRequest = NoOffsetRequest.of()

        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("orderBy", orderBy.toString())
        requestParams.add("category", category.toString())
        requestParams.add("id", noOffsetRequest.id?.toString())
        requestParams.add("limit", noOffsetRequest.limit.toString())

        val resultDto = GetSprinkleResDto(
            sprinkleId = 1,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            category = Category.CAFE,
            expiredAt = LocalDate.now().plusDays(1).toString(),
            sprinkleAt = LocalDateTime.now().plusMinutes(10).toString(),
            participants = 100,
            participateIn = true
        )
        val resultData = listOf(resultDto)
        every { sprinkleService.getSprinkles(userInfoDto, orderBy, category, noOffsetRequest) } returns resultData

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/sprinkles")
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
                MockMvcResultMatchers.jsonPath("\$.data.size()").value(1)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].brandName").value("스타벅스")
            )
            .andDo(
                MockMvcRestDocumentation.document(
                    "뿌리기조회/{methodName}",
                    HeaderDocumentation.requestHeaders(),
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("orderBy").description("정렬 조건"),
                        RequestDocumentation.parameterWithName("category").description("카테고리 종류"),
                        RequestDocumentation.parameterWithName("id").description("마지막으로 전달받은 뿌리기 id"),
                        RequestDocumentation.parameterWithName("limit").description("조회 개수")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                        PayloadDocumentation.fieldWithPath("data[0].sprinkleId").type(JsonFieldType.NUMBER).description("뿌리기 id"),
                        PayloadDocumentation.fieldWithPath("data[0].brandName").type(JsonFieldType.STRING).description("브랜드명"),
                        PayloadDocumentation.fieldWithPath("data[0].merchandiseName").type(JsonFieldType.STRING).description("상품명"),
                        PayloadDocumentation.fieldWithPath("data[0].category").type(JsonFieldType.STRING).description("카테고리"),
                        PayloadDocumentation.fieldWithPath("data[0].expiredAt").type(JsonFieldType.STRING).description("유효기간"),
                        PayloadDocumentation.fieldWithPath("data[0].sprinkleAt").type(JsonFieldType.STRING).description("뿌리기 시간"),
                        PayloadDocumentation.fieldWithPath("data[0].participants").type(JsonFieldType.NUMBER).description("응모자 수"),
                        PayloadDocumentation.fieldWithPath("data[0].participateIn").type(JsonFieldType.BOOLEAN).description("해당 사용자 응모 여부")
                    ),
                    HeaderDocumentation.responseHeaders()
                )
            )
    }

    @Test
    fun `뿌리기 카테고리별 조회 성공`() {
        //given
        val orderBy = OrderBy.CREATED_AT
        val category = Category.CAFE
        val noOffsetRequest = NoOffsetRequest.of()

        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("orderBy", orderBy.toString())
        requestParams.add("category", category.toString())
        requestParams.add("id", noOffsetRequest.id?.toString())
        requestParams.add("limit", noOffsetRequest.limit.toString())

        val resultDto = GetSprinkleResDto(
            sprinkleId = 1,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            category = Category.CAFE,
            expiredAt = LocalDate.now().plusDays(1).toString(),
            sprinkleAt = LocalDateTime.now().plusMinutes(10).toString(),
            participants = 100,
            participateIn = true
        )
        val resultData = listOf(resultDto)
        every { sprinkleService.getSprinkles(userInfoDto, orderBy, category, noOffsetRequest) } returns resultData

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/sprinkles")
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
                MockMvcResultMatchers.jsonPath("\$.data.size()").value(1)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].brandName").value("스타벅스")
            )
            .andDo(
                MockMvcRestDocumentation.document(
                    "뿌리기조회/{methodName}",
                    HeaderDocumentation.requestHeaders(),
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("orderBy").description("정렬 조건"),
                        RequestDocumentation.parameterWithName("category").description("카테고리 종류"),
                        RequestDocumentation.parameterWithName("id").description("마지막으로 전달받은 뿌리기 id"),
                        RequestDocumentation.parameterWithName("limit").description("조회 개수")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                        PayloadDocumentation.fieldWithPath("data[0].sprinkleId").type(JsonFieldType.NUMBER).description("뿌리기 id"),
                        PayloadDocumentation.fieldWithPath("data[0].brandName").type(JsonFieldType.STRING).description("브랜드명"),
                        PayloadDocumentation.fieldWithPath("data[0].merchandiseName").type(JsonFieldType.STRING).description("상품명"),
                        PayloadDocumentation.fieldWithPath("data[0].category").type(JsonFieldType.STRING).description("카테고리"),
                        PayloadDocumentation.fieldWithPath("data[0].expiredAt").type(JsonFieldType.STRING).description("유효기간"),
                        PayloadDocumentation.fieldWithPath("data[0].sprinkleAt").type(JsonFieldType.STRING).description("뿌리기 시간"),
                        PayloadDocumentation.fieldWithPath("data[0].participants").type(JsonFieldType.NUMBER).description("응모자 수"),
                        PayloadDocumentation.fieldWithPath("data[0].participateIn").type(JsonFieldType.BOOLEAN).description("해당 사용자 응모 여부")
                    ),
                    HeaderDocumentation.responseHeaders()
                )
            )
    }

    @Test
    fun `뿌리기 조회 실패 - OrderBy Enum에 해당하는 값이 아닐 때`() {
        //given
        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("orderBy", "OTHER")
        requestParams.add("category", Category.CAFE.toString())
        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/sprinkles")
                .header("Authorization", userInfoDto.inherenceId)
                .queryParams(requestParams)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
    }

    @Test
    fun `뿌리기 조회 실패 - Category Enum에 해당하는 값이 아닐 때`() {
        //given
        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("orderBy", OrderBy.DEADLINE.toString())
        requestParams.add("category", "OTHER")
        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/sprinkles")
                .header("Authorization", userInfoDto.inherenceId)
                .queryParams(requestParams)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
    }

    @Test
    fun `뿌리기 조회 실패 - orderBy=DEADLINE, category!=ALL 일 때`() {
        //given
        val orderBy = OrderBy.DEADLINE
        val category = Category.CAFE

        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("orderBy", orderBy.toString())
        requestParams.add("category", category.toString())

        every { sprinkleService.getSprinkles(userInfoDto, orderBy, category, NoOffsetRequest.of()) } throws (BaseException(ResponseCode.INVALID_INPUT_VALUE))
        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/sprinkles")
                .header("Authorization", userInfoDto.inherenceId)
                .queryParams(requestParams)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
    }

}
package mashup.ggiriggiri.gifticonstorm.presentation

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import mashup.ggiriggiri.gifticonstorm.application.sprinkle.SprinkleService
import mashup.ggiriggiri.gifticonstorm.common.DEFAULT_OBJECT_MAPPER
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.dto.event.CreateEventRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.GetSprinkleResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.SprinkleInfoResDto
import mashup.ggiriggiri.gifticonstorm.infrastructure.SigninBot
import mashup.ggiriggiri.gifticonstorm.presentation.restdocs.TestRestDocs
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
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
import java.time.LocalTime

@WebMvcTest(SprinkleController::class)
internal class SprinkleControllerTest : TestRestDocs() {

    @MockkBean
    private lateinit var sprinkleService: SprinkleService

    @MockkBean
    private lateinit var memberRepository: MemberRepository

    @MockkBean
    private lateinit var signinBot: SigninBot

    private val userInfoDto = UserInfoDto(id = 0, inherenceId = "test-user")

    private val now = LocalDateTime.now()

    @BeforeEach
    fun setUp() {
        every { memberRepository.findByInherenceId(any()) } returns Member(inherenceId = userInfoDto.inherenceId)
    }

    @Test
    fun `뿌리기 쿠폰 등록 성공`() {
        //given
        val createEventRequestDto = CreateEventRequestDto(
            category = Category.CAFE,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            couponExpiredTime = LocalDate.now().plusDays(1).toString(),
            deadlineMinutes = 60
        )

        val image = MockMultipartFile(
            "image",
            "file.png",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "<<image data>>".toByteArray()
        )

        val eventInfo = MockMultipartFile(
            "eventInfo",
            "eventInfo",
            MediaType.APPLICATION_JSON_VALUE,
            DEFAULT_OBJECT_MAPPER.writeValueAsString(createEventRequestDto).toByteArray()
        )

        justRun { sprinkleService.createSprinkle(image, createEventRequestDto, userInfoDto) }

        //when, then
        mockMvc.perform(
            RestDocumentationRequestBuilders.multipart("/api/v1/sprinkle")
                .file(image)
                .file(eventInfo)
                .header("Authorization", userInfoDto.inherenceId)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "뿌리기 등록",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("애플 사용자 고유 id"),
                    ),
                    RequestDocumentation.requestParts(
                        RequestDocumentation.partWithName("image").description("쿠폰 이미지"),
                        RequestDocumentation.partWithName("eventInfo").description("쿠폰 상세정보")
                    ),
                    PayloadDocumentation.requestPartBody("image"),
                    PayloadDocumentation.requestPartBody("eventInfo"),
                    PayloadDocumentation.requestPartFields(
                        "eventInfo",
                        PayloadDocumentation.fieldWithPath("category").type(JsonFieldType.STRING)
                            .description("쿠폰 카테고리"),
                        PayloadDocumentation.fieldWithPath("brandName").type(JsonFieldType.STRING)
                            .description("쿠폰 브랜드명"),
                        PayloadDocumentation.fieldWithPath("merchandiseName").type(JsonFieldType.STRING)
                            .description("쿠폰 상품명"),
                        PayloadDocumentation.fieldWithPath("couponExpiredTime").type(JsonFieldType.STRING)
                            .description("쿠폰 유효기간"),
                        PayloadDocumentation.fieldWithPath("deadlineMinutes").type(JsonFieldType.NUMBER)
                            .description("쿠폰 뿌릴 시간 (몇 분 뒤)")
                    ),
                    HeaderDocumentation.responseHeaders()
                )
            )
    }

    @Test
    fun `뿌리기 쿠폰 등록 실패 - 쿠폰 유효기간 데이터 형식 오류`() {
        //given
        val createEventRequestDto = CreateEventRequestDto(
            category = Category.CAFE,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            couponExpiredTime = "2022/07/06",
            deadlineMinutes = 180
        )

        val image = MockMultipartFile(
            "image",
            "file.png",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "<<image data>>".toByteArray()
        )

        val eventInfo = MockMultipartFile(
            "eventInfo",
            "eventInfo",
            MediaType.APPLICATION_JSON_VALUE,
            DEFAULT_OBJECT_MAPPER.writeValueAsString(createEventRequestDto).toByteArray()
        )
        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/v1/sprinkle")
                .file(image)
                .file(eventInfo)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", userInfoDto.inherenceId)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code),
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].field").value("couponExpiredTime")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].message").value("쿠폰 유효기간 형식(YYYY-MM-DD)을 확인해주세요.")
            )
    }

    @Test
    fun `뿌리기 쿠폰 등록 실패 - 쿠폰 유효기간 지남`() {
        //given
        val createEventRequestDto = CreateEventRequestDto(
            category = Category.CAFE,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            couponExpiredTime = LocalDate.now().minusDays(1).toString(),
            deadlineMinutes = 180
        )

        val image = MockMultipartFile(
            "image",
            "file.png",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "<<image data>>".toByteArray()
        )

        val eventInfo = MockMultipartFile(
            "eventInfo",
            "eventInfo",
            MediaType.APPLICATION_JSON_VALUE,
            DEFAULT_OBJECT_MAPPER.writeValueAsString(createEventRequestDto).toByteArray()
        )

        justRun { sprinkleService.createSprinkle(image, createEventRequestDto, userInfoDto) }

        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/v1/sprinkle")
                .file(image)
                .file(eventInfo)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", userInfoDto.inherenceId)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code),
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].field").value("couponExpiredTime")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].message").value("쿠폰 유효기간이 지났습니다.")
            )
    }

    @Test
    fun `뿌리기 쿠폰 등록 실패 - 뿌리기 시간이 쿠폰 유효기간 넘김`() {
        //given
        val createEventRequestDto = CreateEventRequestDto(
            category = Category.CAFE,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            couponExpiredTime = LocalDate.now().toString(),
            deadlineMinutes = 1440
        )

        val image = MockMultipartFile(
            "image",
            "file.png",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "<<image data>>".toByteArray()
        )

        val eventInfo = MockMultipartFile(
            "eventInfo",
            "eventInfo",
            MediaType.APPLICATION_JSON_VALUE,
            DEFAULT_OBJECT_MAPPER.writeValueAsString(createEventRequestDto).toByteArray()
        )
        //when, then
        mockMvc.perform(
            MockMvcRequestBuilders.multipart("/api/v1/sprinkle")
                .file(image)
                .file(eventInfo)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", userInfoDto.inherenceId)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code),
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].field").value("deadlineMinutes")
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data[0].message").value("뿌리기 시간은 쿠폰 유효기간 이내여야 합니다.")
            )
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
            expiredAt = now.plusDays(1).with(LocalTime.MAX).toString(),
            sprinkleAt = now.plusMinutes(10).toString(),
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
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("애플 사용자 고유 id"),
                    ),
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("orderBy").description("정렬 조건 - 'DEADLINE' 고정값"),
                        RequestDocumentation.parameterWithName("category").description("카테고리 종류 - 'ALL' 고정값")
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
        val noOffsetRequest = NoOffsetRequest.of(1, 10)

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
            expiredAt = now.plusDays(1).with(LocalTime.MAX).toString(),
            sprinkleAt = now.plusMinutes(10).toString(),
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
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("애플 사용자 고유 id"),
                    ),
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("orderBy").description("정렬 조건 - 'CREATED_AT' 고정값"),
                        RequestDocumentation.parameterWithName("category")
                            .description("카테고리 종류 - ALL, CAFE(카페/디저트), DELIVERY(치킨/배달음식), ICECREAM(아이스크림), CONVENIENCE_STORE(편의점), FAST_FOOD(패스트푸드), VOUCHER(금액권), ETC(기타)"),
                        RequestDocumentation.parameterWithName("id").description("마지막으로 전달받은 뿌리기 id (첫 요청시 id = null)"),
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
        val noOffsetRequest = NoOffsetRequest.of(1, 10)

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
            expiredAt = now.plusDays(1).with(LocalTime.MAX).toString(),
            sprinkleAt = now.plusMinutes(10).toString(),
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
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("애플 사용자 고유 id"),
                    ),
                    RequestDocumentation.requestParameters(
                        RequestDocumentation.parameterWithName("orderBy").description("정렬 조건"),
                        RequestDocumentation.parameterWithName("category")
                            .description("카테고리 종류 - ALL, CAFE(카페/디저트), DELIVERY(치킨/배달음식), ICECREAM(아이스크림), CONVENIENCE_STORE(편의점), FAST_FOOD(패스트푸드), VOUCHER(금액권), ETC(기타)"),
                        RequestDocumentation.parameterWithName("id").description("마지막으로 전달받은 뿌리기 id (첫 요청시 id = null)"),
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

    @Test
    fun `뿌리기 응모`() {
        every { sprinkleService.applySprinkle(any(), any()) } returns Unit

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/v1/sprinkle/{sprinkleId}/apply", 1L)
                .header("Authorization", userInfoDto.inherenceId)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "뿌리기응모",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("애플 사용자 고유 id"),
                    ),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("sprinkleId").description("응모 대상 sprinkleId")
                    )
                )
            )
    }
    @Test
    fun `뿌리기 정보 조회 성공`() {
        //given
        val resDto = SprinkleInfoResDto(
            sprinkleId = 1,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            category = Category.CAFE,
            expiredAt = now.plusDays(1).with(LocalTime.MAX).toString(),
            sprinkleAt = now.plusMinutes(10).toString(),
            participants = 100
        )

        every { sprinkleService.getSprinkleInfo(1) } returns resDto

        //when, then
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/sprinkle-info/{id}", 1)
                .header("Authorization", userInfoDto.inherenceId)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.code").value(ResponseCode.OK.code)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message").value(ResponseCode.OK.message)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.data.sprinkleId").value(1)
            )
            .andDo(
                MockMvcRestDocumentation.document(
                    "뿌리기 단건 조회/{methodName}",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("애플 사용자 고유 id"),
                    ),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("id").description("Sprinkle(뿌리기) Id"),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        PayloadDocumentation.fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                        PayloadDocumentation.fieldWithPath("data.sprinkleId").type(JsonFieldType.NUMBER).description("뿌리기 id"),
                        PayloadDocumentation.fieldWithPath("data.brandName").type(JsonFieldType.STRING).description("브랜드명"),
                        PayloadDocumentation.fieldWithPath("data.merchandiseName").type(JsonFieldType.STRING).description("상품명"),
                        PayloadDocumentation.fieldWithPath("data.category").type(JsonFieldType.STRING).description("카테고리"),
                        PayloadDocumentation.fieldWithPath("data.expiredAt").type(JsonFieldType.STRING).description("유효기간"),
                        PayloadDocumentation.fieldWithPath("data.sprinkleAt").type(JsonFieldType.STRING).description("뿌리기 시간"),
                        PayloadDocumentation.fieldWithPath("data.participants").type(JsonFieldType.NUMBER).description("응모자 수"),
                    ),
                    HeaderDocumentation.requestHeaders()
                )
            )
    }

}
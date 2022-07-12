package mashup.ggiriggiri.gifticonstorm.presentation

import mashup.ggiriggiri.gifticonstorm.application.sprinkle.SprinkleService
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.GetSprinkleResDto
import mashup.ggiriggiri.gifticonstorm.presentation.restdocs.TestRestDocs
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.time.LocalDate
import java.time.LocalDateTime

@WebMvcTest(SprinkleController::class)
internal class SprinkleControllerGetSprinkleTest : TestRestDocs() {

    @MockBean
    private lateinit var sprinkleService: SprinkleService

    @Test
    fun `뿌리기 마감임박 조회 성공`() {
        //given
        val orderBy: OrderBy = OrderBy.DEADLINE
        val category: Category = Category.ALL

        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("orderBy", orderBy.toString())
        requestParams.add("category", category.toString())

        val resultDto = GetSprinkleResDto(
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            category = Category.CAFE,
            expiredAt = LocalDate.now().plusDays(1).toString(),
            sprinkleAt = LocalDateTime.now().plusMinutes(10).toString(),
            participants = 100,
            participateIn = true
        )
        val resultData = listOf(resultDto)
        Mockito.`when`(sprinkleService.getSprinkles(orderBy, category)).thenReturn(resultData)

        //when, then
        mockMvc.perform(
            get("/api/v1/sprinkles")
                .queryParams(requestParams)
        )
            .andDo(MockMvcResultHandlers.print())
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
                MockMvcResultMatchers.jsonPath("\$.data[0].brandName").value(resultDto.brandName)
            )
    }

    @Test
    fun `뿌리기 전체 조회 성공`() {
        //given
        val orderBy = OrderBy.CREATED_AT
        val category = Category.ALL

        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("orderBy", orderBy.toString())
        requestParams.add("category", category.toString())

        val resultDto = GetSprinkleResDto(
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            category = Category.CAFE,
            expiredAt = LocalDate.now().plusDays(1).toString(),
            sprinkleAt = LocalDateTime.now().plusMinutes(10).toString(),
            participants = 100,
            participateIn = true
        )
        val resultData = listOf(resultDto)
        Mockito.`when`(sprinkleService.getSprinkles(orderBy, category)).thenReturn(resultData)

        //when, then
        mockMvc.perform(
            get("/api/v1/sprinkles")
                .queryParams(requestParams)
        )
            .andDo(MockMvcResultHandlers.print())
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
                MockMvcResultMatchers.jsonPath("\$.data[0].brandName").value(resultDto.brandName)
            )
    }

    @Test
    fun `뿌리기 카테고리별 조회 성공`() {
        //given
        val orderBy = OrderBy.CREATED_AT
        val category = Category.CAFE

        val requestParams: MultiValueMap<String, String> = LinkedMultiValueMap()
        requestParams.add("orderBy", orderBy.toString())
        requestParams.add("category", category.toString())

        val resultDto = GetSprinkleResDto(
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            category = Category.CAFE,
            expiredAt = LocalDate.now().plusDays(1).toString(),
            sprinkleAt = LocalDateTime.now().plusMinutes(10).toString(),
            participants = 100,
            participateIn = true
        )
        val resultData = listOf(resultDto)
        Mockito.`when`(sprinkleService.getSprinkles(orderBy, category)).thenReturn(resultData)

        //when, then
        mockMvc.perform(
            get("/api/v1/sprinkles")
                .queryParams(requestParams)
        )
            .andDo(MockMvcResultHandlers.print())
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
                MockMvcResultMatchers.jsonPath("\$.data[0].brandName").value(resultDto.brandName)
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
            get("/api/v1/sprinkles")
                .queryParams(requestParams)
        )
            .andDo(MockMvcResultHandlers.print())
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
            get("/api/v1/sprinkles")
                .queryParams(requestParams)
        )
            .andDo(MockMvcResultHandlers.print())
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

        Mockito.`when`(sprinkleService.getSprinkles(orderBy, category)).thenThrow(BaseException(ResponseCode.INVALID_INPUT_VALUE))
        //when, then
        mockMvc.perform(
            get("/api/v1/sprinkles")
                .queryParams(requestParams)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code)
            )
            .andExpect(
                MockMvcResultMatchers.jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
    }

}
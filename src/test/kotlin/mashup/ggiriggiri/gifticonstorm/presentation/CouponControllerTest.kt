package mashup.ggiriggiri.gifticonstorm.presentation

import com.ninjasquad.springmockk.MockkBean
import io.mockk.justRun
import mashup.ggiriggiri.gifticonstorm.application.CouponService
import mashup.ggiriggiri.gifticonstorm.common.DEFAULT_OBJECT_MAPPER
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.dto.CouponSaveRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.presentation.restdocs.TestRestDocs
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.partWithName
import org.springframework.restdocs.request.RequestDocumentation.requestParts
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@WebMvcTest(CouponController::class)
internal class CouponControllerTest : TestRestDocs() {

    @MockkBean
    private lateinit var couponService: CouponService

    @MockkBean
    private lateinit var memberRepository: MemberRepository

    @Test
    fun `뿌리기 쿠폰 등록 성공`() {
        //given
        val requestDto = CouponSaveRequestDto(
            category = Category.CAFE,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            couponExpiredTime = LocalDate.now().plusDays(1).toString(),
            sprinkleTime = 1L
        )

        val image = MockMultipartFile(
            "image",
            "file.png",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "<<image data>>".toByteArray()
        )

        val couponInfo = MockMultipartFile(
             "couponInfo",
            "couponInfo",
            MediaType.APPLICATION_JSON_VALUE,
            DEFAULT_OBJECT_MAPPER.writeValueAsString(requestDto).toByteArray()
        )

        justRun { couponService.saveCoupon(image, requestDto) }

        //when, then
        mockMvc.perform(
            multipart("/api/v1/sprinkle")
                .file(image)
                .file(couponInfo)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andDo(
                document("뿌리기 등록",
                    requestHeaders(),
                    requestParts(
                        partWithName("image").description("쿠폰 이미지"),
                        partWithName("couponInfo").description("쿠폰 상세정보")
                    ),
                    requestPartBody("image"),
                    requestPartBody("couponInfo"),
                    requestPartFields("couponInfo",
                        fieldWithPath("category").type(JsonFieldType.STRING).description("쿠폰 카테고리"),
                        fieldWithPath("brandName").type(JsonFieldType.STRING).description("쿠폰 브랜드명"),
                        fieldWithPath("merchandiseName").type(JsonFieldType.STRING).description("쿠폰 상품명"),
                        fieldWithPath("couponExpiredTime").type(JsonFieldType.STRING).description("쿠폰 유효기간"),
                        fieldWithPath("sprinkleTime").type(JsonFieldType.NUMBER).description("쿠폰 뿌릴 시간 (몇 시간 뒤)")
                    ),
                    responseFields(
                        fieldWithPath("code").type(JsonFieldType.STRING).description("응답 코드"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메세지"),
                        fieldWithPath("data").type(JsonFieldType.NULL).description("응답 데이터")
                    ),
                    responseHeaders()
                )
            )
    }

    @Test
    fun `뿌리기 쿠폰 등록 실패 - 쿠폰 유효기간 데이터 형식 오류`() {
        //given
        val requestDto = CouponSaveRequestDto(
            category = Category.CAFE,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            couponExpiredTime = "2022/07/06",
            sprinkleTime = 3L
        )

        val image = MockMultipartFile(
            "image",
            "file.png",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "<<image data>>".toByteArray()
        )

        val couponInfo = MockMultipartFile(
            "couponInfo",
            "couponInfo",
            MediaType.APPLICATION_JSON_VALUE,
            DEFAULT_OBJECT_MAPPER.writeValueAsString(requestDto).toByteArray()
        )
        //when, then
        mockMvc.perform(
            multipart("/api/v1/sprinkle")
                .file(image)
                .file(couponInfo)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(
                jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code),
            )
            .andExpect(
                jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
            .andExpect(
                jsonPath("\$.data[0].field").value("couponExpiredTime")
            )
            .andExpect(
                jsonPath("\$.data[0].message").value("쿠폰 유효기간 형식(YYYY-MM-DD)을 확인해주세요.")
            )
    }

    @Test
    fun `뿌리기 쿠폰 등록 실패 - 쿠폰 유효기간 지남`() {
        //given
        val requestDto = CouponSaveRequestDto(
            category = Category.CAFE,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            couponExpiredTime = LocalDate.now().minusDays(1).toString(),
            sprinkleTime = 3L
        )

        val image = MockMultipartFile(
            "image",
            "file.png",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "<<image data>>".toByteArray()
        )

        val couponInfo = MockMultipartFile(
            "couponInfo",
            "couponInfo",
            MediaType.APPLICATION_JSON_VALUE,
            DEFAULT_OBJECT_MAPPER.writeValueAsString(requestDto).toByteArray()
        )
        //when, then
        mockMvc.perform(
            multipart("/api/v1/sprinkle")
                .file(image)
                .file(couponInfo)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(
                jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code),
            )
            .andExpect(
                jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
            .andExpect(
                jsonPath("\$.data[0].field").value("couponExpiredTime")
            )
            .andExpect(
                jsonPath("\$.data[0].message").value("쿠폰 유효기간이 지났습니다.")
            )
    }

    @Test
    fun `뿌리기 쿠폰 등록 실패 - 뿌리기 시간이 쿠폰 유효기간 넘김`() {
        //given
        val requestDto = CouponSaveRequestDto(
            category = Category.CAFE,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            couponExpiredTime = LocalDate.now().toString(),
            sprinkleTime = 24L
        )

        val image = MockMultipartFile(
            "image",
            "file.png",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "<<image data>>".toByteArray()
        )

        val couponInfo = MockMultipartFile(
            "couponInfo",
            "couponInfo",
            MediaType.APPLICATION_JSON_VALUE,
            DEFAULT_OBJECT_MAPPER.writeValueAsString(requestDto).toByteArray()
        )
        //when, then
        mockMvc.perform(
            multipart("/api/v1/sprinkle")
                .file(image)
                .file(couponInfo)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isBadRequest)
            .andExpect(
                jsonPath("\$.code").value(ResponseCode.INVALID_INPUT_VALUE.code),
            )
            .andExpect(
                jsonPath("\$.message").value(ResponseCode.INVALID_INPUT_VALUE.message)
            )
            .andExpect(
                jsonPath("\$.data[0].field").value("sprinkleTime")
            )
            .andExpect(
                jsonPath("\$.data[0].message").value("뿌리기 시간은 쿠폰 유효기간 이내여야 합니다.")
            )
    }
}
package mashup.ggiriggiri.gifticonstorm.application

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.dto.CouponSaveRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.repository.CouponRepository
import mashup.ggiriggiri.gifticonstorm.infrastructure.S3ImageUploader
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@ActiveProfiles("test")
@Transactional(readOnly = true)
@SpringBootTest
internal class CouponServiceTest {

    @Autowired
    private lateinit var couponService: CouponService

    @Autowired
    private lateinit var couponRepository: CouponRepository

    @MockBean
    private lateinit var imageUploader: S3ImageUploader

    @Test
    fun `뿌릴 쿠폰 등록`() {
        //given
        val image = MockMultipartFile(
            "image",
            "image.png",
            "image/png",
            "<<png data>>".toByteArray()
        )
        val requestDto = CouponSaveRequestDto(
            category = Category.CAFE,
            brandName = "스타벅스",
            merchandiseName = "아이스 아메리카노",
            couponExpiredTime = "2022-07-06",
            sprinkleTime = 3L
        )
        //when
        given(imageUploader.upload(image)).willReturn("testImageUrl")
        couponService.saveCoupon(image, requestDto)
        //then
        val coupons = couponRepository.findAll()
        assertEquals(1, coupons.size)
        assertEquals("스타벅스", coupons[0].brandName)
    }
}
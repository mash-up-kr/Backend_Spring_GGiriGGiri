package mashup.ggiriggiri.gifticonstorm.application

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.coupon.dto.CouponSaveRequestDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.repository.CouponRepository
import mashup.ggiriggiri.gifticonstorm.infrastructure.S3ImageUploader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Transactional(readOnly = true)
@Service
class CouponService(
    private val s3ImageUploader: S3ImageUploader,
    private val couponRepository: CouponRepository
){

    @Transactional
    fun saveCoupon(image: MultipartFile, saveRequestDto: CouponSaveRequestDto) {
        val imageUrl = s3ImageUploader.upload(image)
        val coupon = Coupon.of(imageUrl, saveRequestDto)
        couponRepository.save(coupon)
    }
}
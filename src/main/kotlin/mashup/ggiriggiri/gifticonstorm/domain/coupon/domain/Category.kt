package mashup.ggiriggiri.gifticonstorm.domain.coupon.domain

enum class Category(
        private val description: String,
) {

    CAFE("음료/디저트"),
    DELIVERY("치킨/배달음식"),
    ICECREAM("아이스크림"),
    CONVENIENCE_STORE("편의점"),
    FAST_FOOD("패스트푸드"),
    VOUCHER("상품권"),
    ETC("기타")
}
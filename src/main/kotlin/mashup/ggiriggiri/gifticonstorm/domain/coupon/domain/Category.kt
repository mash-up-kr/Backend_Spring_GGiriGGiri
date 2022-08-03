package mashup.ggiriggiri.gifticonstorm.domain.coupon.domain

enum class Category(
    private val description: String,
) {

    ALL("전체"),
    CAFE("카페/디저트"),
    DELIVERY("치킨/배달음식"),
    ICECREAM("아이스크림"),
    CONVENIENCE_STORE("편의점"),
    FAST_FOOD("패스트푸드"),
    VOUCHER("금액권"),
    ETC("기타");

    companion object {
        val categories = Category.values()
    }
}
package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository

import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.config.QuerydslTestConfig
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.coupon.repository.CouponRepository
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import java.time.LocalTime

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslTestConfig::class)
class SprinkleRepositoryCustomImplTest @Autowired constructor(
    private val sprinkleRepository: SprinkleRepository,
    private val couponRepository: CouponRepository,
    private val memberRepository: MemberRepository,
    private val participantRepository: ParticipantRepository
) {

    private lateinit var memberList: List<Member>
    private lateinit var couponList: List<Coupon>
    private lateinit var sprinkleList: List<Sprinkle>
    private lateinit var participantList: List<Participant>

    private val now = LocalDateTime.now()

    @BeforeEach
    fun setUp() {
        memberList = mutableListOf(
            Member(inherenceId = "testUser1"),
            Member(inherenceId = "testUser2"),
            Member(inherenceId = "testUser3")
        )
        couponList = mutableListOf(
            Coupon(
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                expiredAt = now.plusDays(1).with(LocalTime.MAX),
                imageUrl = "testUrl",
                category = Category.CAFE,
                member = memberList[0]
            ),
            Coupon(
                brandName = "BHC",
                merchandiseName = "치킨",
                expiredAt = now.plusDays(1).with(LocalTime.MAX),
                imageUrl = "testUrl",
                category = Category.DELIVERY,
                member = memberList[0]
            ),
            Coupon(
                brandName = "베스킨라빈스",
                merchandiseName = "파인트",
                expiredAt = now.plusDays(1).with(LocalTime.MAX),
                imageUrl = "testUrl",
                category = Category.ICECREAM,
                member = memberList[0]
            ),
            Coupon(
                brandName = "버거킹",
                merchandiseName = "햄버거",
                expiredAt = now.plusDays(1).with(LocalTime.MAX),
                imageUrl = "testUrl",
                category = Category.FAST_FOOD,
                member = memberList[0]
            )
        )
        sprinkleList = mutableListOf(
            Sprinkle( //마감임박 해당 데이터
                member = memberList[0],
                coupon = couponList[0],
                sprinkleAt = now.plusMinutes(10)
            ),
            Sprinkle( //마감임박 해당 X 데이터
                member = memberList[0],
                coupon = couponList[1],
                sprinkleAt = now.plusMinutes(11)
            ),
            Sprinkle( //마감임박 해당 데이터
                member = memberList[0],
                coupon = couponList[2],
                sprinkleAt = now.plusMinutes(9)
            ),
            Sprinkle( //마감임박 해당 데이터
                member = memberList[0],
                coupon = couponList[3],
                sprinkleAt = now.plusMinutes(8)
            )
        )
        participantList = mutableListOf(
            Participant(member = memberList[1], sprinkle = sprinkleList[0]),
            Participant(member = memberList[2], sprinkle = sprinkleList[0]),
            Participant(member = memberList[1], sprinkle = sprinkleList[1]),
            Participant(member = memberList[1], sprinkle = sprinkleList[2])
        )
        memberRepository.saveAll(memberList)
        couponRepository.saveAll(couponList)
        sprinkleRepository.saveAll(sprinkleList)
        participantRepository.saveAll(participantList)
    }

    @Test
    fun `findAllByDeadLine - 뿌리기 남은 시간 10분 이내 & 참여자 수 내림차순 중 상위 2개 조회`() {
        //when
        val sprinkleListVos = sprinkleRepository.findAllByDeadLine(10, 2)
        //then
        assertThat(sprinkleListVos.size).isEqualTo(2)
        assertThat(sprinkleListVos[0].brandName).isEqualTo("스타벅스")
        assertThat(sprinkleListVos[0].participants).isEqualTo(2)
        assertThat(sprinkleListVos[1].brandName).isEqualTo("베스킨라빈스")
        assertThat(sprinkleListVos[1].participants).isEqualTo(1)
    }

    @Test
    fun `findAllByCategory - 전체 조회, NoOffset 첫 번째 페이지`() {
        //given
        val noOffsetRequest = NoOffsetRequest.of(id = null, limit = 2)
        //when
        val sprinkleListVos = sprinkleRepository.findAllByCategory(Category.ALL, noOffsetRequest)
        //then
        assertThat(sprinkleListVos).hasSize(2)
        assertThat(sprinkleListVos[0].brandName).isEqualTo("스타벅스")
        assertThat(sprinkleListVos[0].participants).isEqualTo(2)
        assertThat(sprinkleListVos[1].brandName).isEqualTo("BHC")
        assertThat(sprinkleListVos[1].participants).isEqualTo(1)
    }

    @Test
    fun `findAllByCategory - 전체 조회, NoOffset 두 번째 페이지`() {
        //given
        val noOffsetRequest = NoOffsetRequest.of(id = sprinkleList[1].id, limit = 2)
        //when
        val sprinkleListVos = sprinkleRepository.findAllByCategory(Category.ALL, noOffsetRequest)
        //then
        assertThat(sprinkleListVos).hasSize(2)
        assertThat(sprinkleListVos[0].brandName).isEqualTo("베스킨라빈스")
        assertThat(sprinkleListVos[0].participants).isEqualTo(1)
        assertThat(sprinkleListVos[1].brandName).isEqualTo("버거킹")
        assertThat(sprinkleListVos[1].participants).isEqualTo(0)
    }

    @Test
    fun `findAllByCategory - 특정 카테고리 조회`() {
        //when
        val sprinkleListVos = sprinkleRepository.findAllByCategory(Category.CAFE, NoOffsetRequest.of())
        //then
        assertThat(sprinkleListVos).hasSize(1)
        assertThat(sprinkleListVos[0].brandName).isEqualTo("스타벅스")
        assertThat(sprinkleListVos[0].participants).isEqualTo(2)
    }

    @Test
    fun `findInfoById - 뿌리기 정보 조회`() {
        //when
        val sprinkleInfoVo = sprinkleRepository.findInfoById(sprinkleList[0].id)
        //then
        assertThat(sprinkleInfoVo!!.brandName).isEqualTo("스타벅스")
        assertThat(sprinkleInfoVo.participants).isEqualTo(2)
    }

    @Test
    fun `findRegistHistoryByMemberId - 쿠폰 등록 내역 조회`() {
        //given
        val noOffsetRequest = NoOffsetRequest.of(id = null, limit = 2)
        //when
        val registHistoryVos = sprinkleRepository.findRegistHistoryByMemberId(memberList[0].id, noOffsetRequest)
        //then
        assertThat(registHistoryVos).hasSize(2)
        assertThat(registHistoryVos[0].brandName).isEqualTo("버거킹")
        assertThat(registHistoryVos[1].brandName).isEqualTo("베스킨라빈스")
    }
}
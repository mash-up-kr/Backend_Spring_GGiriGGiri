package mashup.ggiriggiri.gifticonstorm.domain.participant.repository

import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.config.QuerydslTestConfig
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.coupon.repository.CouponRepository
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member

import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime

@DataJpaTest
@ActiveProfiles("test")
@Import(QuerydslTestConfig::class)
class ParticipantRepositoryCustomImplTest @Autowired constructor(
    private val sprinkleRepository: SprinkleRepository,
    private val couponRepository: CouponRepository,
    private val memberRepository: MemberRepository,
    private val participantRepository: ParticipantRepository
) {

    private lateinit var memberList: List<Member>
    private lateinit var couponList: List<Coupon>
    private lateinit var sprinkleList: List<Sprinkle>
    private lateinit var participantList: List<Participant>

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
                expiredAt = LocalDateTime.now().plusDays(1),
                imageUrl = "testUrl",
                category = Category.CAFE,
                member = memberList[0]
            ),
            Coupon(
                brandName = "BHC",
                merchandiseName = "치킨",
                expiredAt = LocalDateTime.now().plusDays(1),
                imageUrl = "testUrl",
                category = Category.DELIVERY,
                member = memberList[0]
            )
        )
        sprinkleList = mutableListOf(
            Sprinkle(
                member = memberList[0],
                coupon = couponList[0],
                sprinkleAt = LocalDateTime.now().plusMinutes(10)
            ),
            Sprinkle(
                member = memberList[0],
                coupon = couponList[1],
                sprinkleAt = LocalDateTime.now().plusMinutes(11)
            )
        )
        participantList = mutableListOf(
            Participant(member = memberList[1], sprinkle = sprinkleList[0]),
            Participant(member = memberList[2], sprinkle = sprinkleList[0]),
            Participant(member = memberList[1], sprinkle = sprinkleList[1]),
        )
        memberRepository.saveAll(memberList)
        couponRepository.saveAll(couponList)
        sprinkleRepository.saveAll(sprinkleList)
        participantRepository.saveAll(participantList)
    }

    @Test
    fun `특정 사용자가 응모한 뿌리기의 id 리스트 반환`() {
        //when
        val sprinkleIdList = participantRepository.findAllSprinkleIdByMemberId(memberList[1].id)
        //then
        assertThat(sprinkleIdList.size).isEqualTo(2)
        assertThat(sprinkleIdList[0]).isEqualTo(sprinkleList[0].id)
        assertThat(sprinkleIdList[1]).isEqualTo(sprinkleList[1].id)
    }

    @Test
    fun `findHistoryByMemberId - 응모 내역 조회`() {
        //given
        val noOffsetRequest = NoOffsetRequest.of()
        //when
        val infoVos = participantRepository.findHistoryByMemberId(memberList[1].id, noOffsetRequest)
        //then
        assertThat(infoVos.size).isEqualTo(2)
        assertThat(infoVos[0].brandName).isEqualTo("BHC")
        assertThat(infoVos[1].brandName).isEqualTo("스타벅스")
    }
}
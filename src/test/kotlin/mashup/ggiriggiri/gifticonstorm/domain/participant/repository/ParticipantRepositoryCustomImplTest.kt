package mashup.ggiriggiri.gifticonstorm.domain.participant.repository

import mashup.ggiriggiri.gifticonstorm.config.QuerydslTestConfig
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.coupon.repository.CouponRepository
import mashup.ggiriggiri.gifticonstorm.domain.member.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import org.assertj.core.api.Assertions.assertThat
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

    @Test
    fun `특정 사용자가 응모한 뿌리기의 id 리스트 반환`() {
        //given
        val memberList = mutableListOf(
            Member(inherenceId = "testUser1"),
            Member(inherenceId = "testUser2"),
            Member(inherenceId = "testUser3")
        )
        val couponList = mutableListOf(
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
        val sprinkleList = mutableListOf(
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
        val participantList = mutableListOf(
            Participant(member = memberList[1], sprinkle = sprinkleList[0]),
            Participant(member = memberList[2], sprinkle = sprinkleList[0]),
            Participant(member = memberList[1], sprinkle = sprinkleList[1]),
        )
        memberRepository.saveAll(memberList)
        couponRepository.saveAll(couponList)
        sprinkleRepository.saveAll(sprinkleList)
        participantRepository.saveAll(participantList)
        //when
        val sprinkleIdList = participantRepository.findAllSprinkleIdByMemberId(memberList[1].id)
        //then
        assertThat(sprinkleIdList.size).isEqualTo(2)
        assertThat(sprinkleIdList[0]).isEqualTo(1)
        assertThat(sprinkleIdList[1]).isEqualTo(2)
    }
}
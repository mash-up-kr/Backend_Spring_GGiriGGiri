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

    @BeforeEach
    fun setUp() {
        memberList = mutableListOf(
            Member(inherenceId = "testUser1"),
            Member(inherenceId = "testUser2"),
            Member(inherenceId = "testUser3")
        )
        couponList = mutableListOf(
            Coupon(
                brandName = "????????????",
                merchandiseName = "????????? ???????????????",
                expiredAt = LocalDateTime.now().plusDays(1),
                imageUrl = "testUrl",
                category = Category.CAFE,
                member = memberList[0]
            ),
            Coupon(
                brandName = "BHC",
                merchandiseName = "??????",
                expiredAt = LocalDateTime.now().plusDays(1),
                imageUrl = "testUrl",
                category = Category.DELIVERY,
                member = memberList[0]
            ),
            Coupon(
                brandName = "??????????????????",
                merchandiseName = "?????????",
                expiredAt = LocalDateTime.now().plusDays(1),
                imageUrl = "testUrl",
                category = Category.ICECREAM,
                member = memberList[0]
            ),
            Coupon(
                brandName = "?????????",
                merchandiseName = "?????????",
                expiredAt = LocalDateTime.now().plusDays(1),
                imageUrl = "testUrl",
                category = Category.FAST_FOOD,
                member = memberList[0]
            )
        )
        sprinkleList = mutableListOf(
            Sprinkle( //???????????? ?????? ?????????
                member = memberList[0],
                coupon = couponList[0],
                sprinkleAt = LocalDateTime.now().plusMinutes(10)
            ),
            Sprinkle( //???????????? ?????? X ?????????
                member = memberList[0],
                coupon = couponList[1],
                sprinkleAt = LocalDateTime.now().plusMinutes(11)
            ),
            Sprinkle( //???????????? ?????? ?????????
                member = memberList[0],
                coupon = couponList[2],
                sprinkleAt = LocalDateTime.now().plusMinutes(9)
            ),
            Sprinkle( //???????????? ?????? ?????????
                member = memberList[0],
                coupon = couponList[3],
                sprinkleAt = LocalDateTime.now().plusMinutes(8)
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
    fun `findAllByDeadLine - ????????? ?????? ?????? 10??? ?????? & ????????? ??? ???????????? ??? ?????? 2??? ??????`() {
        //when
        val sprinkleListVos = sprinkleRepository.findAllByDeadLine(10, 2)
        //then
        assertThat(sprinkleListVos.size).isEqualTo(2)
        assertThat(sprinkleListVos[0].brandName).isEqualTo("????????????")
        assertThat(sprinkleListVos[0].participants).isEqualTo(2)
        assertThat(sprinkleListVos[1].brandName).isEqualTo("??????????????????")
        assertThat(sprinkleListVos[1].participants).isEqualTo(1)
    }

    @Test
    fun `findAllByCategory - ?????? ??????, NoOffset ??? ?????? ?????????`() {
        //given
        val noOffsetRequest = NoOffsetRequest.of(id = null, limit = 2)
        //when
        val sprinkleListVos = sprinkleRepository.findAllByCategory(Category.ALL, noOffsetRequest)
        //then
        assertThat(sprinkleListVos).hasSize(2)
        assertThat(sprinkleListVos[0].brandName).isEqualTo("????????????")
        assertThat(sprinkleListVos[0].participants).isEqualTo(2)
        assertThat(sprinkleListVos[1].brandName).isEqualTo("BHC")
        assertThat(sprinkleListVos[1].participants).isEqualTo(1)
    }

    @Test
    fun `findAllByCategory - ?????? ??????, NoOffset ??? ?????? ?????????`() {
        //given
        val noOffsetRequest = NoOffsetRequest.of(id = sprinkleList[1].id, limit = 2)
        //when
        val sprinkleListVos = sprinkleRepository.findAllByCategory(Category.ALL, noOffsetRequest)
        //then
        assertThat(sprinkleListVos).hasSize(2)
        assertThat(sprinkleListVos[0].brandName).isEqualTo("??????????????????")
        assertThat(sprinkleListVos[0].participants).isEqualTo(1)
        assertThat(sprinkleListVos[1].brandName).isEqualTo("?????????")
        assertThat(sprinkleListVos[1].participants).isEqualTo(0)
    }

    @Test
    fun `findAllByCategory - ?????? ???????????? ??????`() {
        //when
        val sprinkleListVos = sprinkleRepository.findAllByCategory(Category.CAFE, NoOffsetRequest.of())
        //then
        assertThat(sprinkleListVos).hasSize(1)
        assertThat(sprinkleListVos[0].brandName).isEqualTo("????????????")
        assertThat(sprinkleListVos[0].participants).isEqualTo(2)
    }
}
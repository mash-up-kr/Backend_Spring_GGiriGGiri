package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import mashup.ggiriggiri.gifticonstorm.application.CouponService
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.SprinkledStatus
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.GetSprinkleVo
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleInfoVo
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleRegistHistoryVo
import java.time.LocalDateTime
import java.time.LocalTime

internal class SprinkleServiceTest : FunSpec({

    val sprinkleCache = mockk<SprinkleCache>()
    val sprinkleRepository = mockk<SprinkleRepository>()
    val participantRepository = mockk<ParticipantRepository>()
    val couponService = mockk<CouponService>()
    val memberRepository = mockk<MemberRepository>()
    val sprinkleService = SprinkleService(sprinkleCache, sprinkleRepository, participantRepository, couponService, memberRepository)

    context("뿌리기 마감임박 조회") {
        test("성공") {
            //given
            every { sprinkleRepository.findAllByDeadLine(10, 4) } returns getSprinkleVos
            every { participantRepository.findAllSprinkleIdByMemberId(userInfoDto.id) } returns listOf(1L)

            //when
            val resDtos = sprinkleService.getSprinkles(userInfoDto, OrderBy.DEADLINE, Category.ALL, noOffsetRequest)

            //then
            resDtos.size shouldBe 2
            resDtos[0].sprinkleId shouldBe 1
            resDtos[0].brandName shouldBe "스타벅스"
            resDtos[0].participateIn shouldBe true
            resDtos[1].sprinkleId shouldBe 2
            resDtos[1].brandName shouldBe "베스킨라빈스"
            resDtos[1].participateIn shouldBe false
        }

        test("실패 - orderBy=DEADLINE, category!=ALL 일 때") {
            shouldThrow<BaseException> {
                sprinkleService.getSprinkles(userInfoDto, OrderBy.DEADLINE, Category.CAFE, noOffsetRequest)
            }
        }
    }

    context("뿌리기 카테고리별 조회") {
        test("전체 조회 성공") {
            //given
            val category = Category.ALL

            every { sprinkleRepository.findAllByCategory(category, noOffsetRequest) } returns getSprinkleVos
            every { participantRepository.findAllSprinkleIdByMemberId(1) } returns listOf(1L)

            //when
            val resDtos = sprinkleService.getSprinkles(userInfoDto, OrderBy.CREATED_AT, category, noOffsetRequest)

            //then
            resDtos.size shouldBe 2
            resDtos[0].sprinkleId shouldBe 1
            resDtos[0].brandName shouldBe "스타벅스"
            resDtos[0].participateIn shouldBe true
            resDtos[1].sprinkleId shouldBe 2
            resDtos[1].brandName shouldBe "베스킨라빈스"
            resDtos[1].participateIn shouldBe false
        }

        test("특정 카테고리 조회 성공") {
            //given
            val category = Category.CAFE

            every { sprinkleRepository.findAllByCategory(category, noOffsetRequest) } returns listOf(getSprinkleVos[0])
            every { participantRepository.findAllSprinkleIdByMemberId(1) } returns listOf(1L)

            //when
            val resDtos = sprinkleService.getSprinkles(userInfoDto, OrderBy.CREATED_AT, category, noOffsetRequest)

            //then
            resDtos.size shouldBe 1
            resDtos[0].sprinkleId shouldBe 1
            resDtos[0].brandName shouldBe "스타벅스"
            resDtos[0].participateIn shouldBe true
        }

        test("실패 - orderBy=null, category=null 일 때") {
            shouldThrow<BaseException> {
                sprinkleService.getSprinkles(userInfoDto, null, null, NoOffsetRequest.of())
            }
        }
    }

    context("뿌리기 정보 조회") {
        test("성공") {
            //given
            every { sprinkleRepository.findInfoById(1) } returns sprinkleInfoVos[0]
            every { participantRepository.findAllMemberIdBySprinkleId(1) } returns listOf(2)

            //when
            val resDto = sprinkleService.getSprinkleInfo(1, userInfoDto)

            //then
            resDto.sprinkleId shouldBe 1
            resDto.brandName shouldBe "스타벅스"
            resDto.merchandiseName shouldBe "아이스 아메리카노"
            resDto.category shouldBe "카페/디저트"
            resDto.expiredAt shouldBe now.plusDays(1).with(LocalTime.MAX).toString()
            resDto.participants shouldBe 1
            resDto.sprinkleAt shouldBe now.plusMinutes(10).toString()
            resDto.registeredBy shouldBe true
            resDto.participateIn shouldBe false
        }

        test("실패 - 해당 뿌리기가 존재하지 않을 때") {
            every { sprinkleRepository.findInfoById(1) } returns null

            shouldThrow<BaseException> {
                sprinkleService.getSprinkleInfo(1, userInfoDto)
            }
        }
    }


    context("뿌리기 등록 내역 조회") {
        test("성공") {
            //given
            every { sprinkleRepository.findRegistHistoryByMemberId(1, noOffsetRequest) } returns sprinkleRegistHistoryListVos

            //when
            val resDtos = sprinkleService.getSprinkleRegistHistory(userInfoDto, noOffsetRequest)

            //then
            resDtos.size shouldBe 1
            resDtos[0].brandName shouldBe "스타벅스"
            resDtos[0].sprinkledStatus shouldBe SprinkledStatus.PROGRESS
        }
    }

}) {
    companion object {
        private val userInfoDto = UserInfoDto(id = 1, inherenceId = "test-user")

        private val noOffsetRequest = NoOffsetRequest.of()

        private val now = LocalDateTime.now()
        private val getSprinkleVos = listOf(
            GetSprinkleVo(
                sprinkleId = 1,
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                category = Category.CAFE,
                expiredAt = now.plusDays(1).with(LocalTime.MAX),
                participants = 3,
                sprinkleAt = now.plusMinutes(10)
            ),
            GetSprinkleVo(
                sprinkleId = 2,
                brandName = "베스킨라빈스",
                merchandiseName = "파인트",
                category = Category.ICECREAM,
                expiredAt = now.plusDays(1).with(LocalTime.MAX),
                participants = 2,
                sprinkleAt = now.plusMinutes(9)
            )
        )

        private val sprinkleInfoVos = listOf(
            SprinkleInfoVo(
                sprinkleId = 1,
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                category = Category.CAFE,
                expiredAt = now.plusDays(1).with(LocalTime.MAX),
                sprinkleAt = now.plusMinutes(10),
                memberId = 1
            ),
            SprinkleInfoVo(
                sprinkleId = 2,
                brandName = "베스킨라빈스",
                merchandiseName = "파인트",
                category = Category.ICECREAM,
                expiredAt = now.plusDays(1).with(LocalTime.MAX),
                sprinkleAt = now.plusMinutes(9),
                memberId = 2
            )
        )

        private val member = Member(inherenceId = "test-user")
        private val coupon = Coupon(
            brandName = "스타벅스",
            merchandiseName = "아이스아메리카노",
            expiredAt = now.plusDays(1),
            imageUrl = "/test/url",
            category = Category.CAFE,
            member = member
        )
        private val sprinkle = Sprinkle(
            member = member,
            coupon = coupon,
            sprinkleAt = now.plusMinutes(10)
        )

        private val sprinkleRegistHistoryListVos = listOf(
            SprinkleRegistHistoryVo(
                sprinkleId = 1,
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                expiredAt = LocalDateTime.now().plusDays(1).with(LocalTime.MAX),
                category = Category.CAFE,
                participants = 100,
                sprinkled = false,
                sprinkleAt = LocalDateTime.now().plusMinutes(10)
            )
        )
    }
}
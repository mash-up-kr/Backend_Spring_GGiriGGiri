package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleListVo
import java.time.LocalDateTime

internal class SprinkleServiceTest : FunSpec({

    val sprinkleRepository = mockk<SprinkleRepository>()
    val participantRepository = mockk<ParticipantRepository>()
    val sprinkleCache = mockk<SprinkleCache>()
    val sprinkleService = SprinkleService(sprinkleCache, sprinkleRepository, participantRepository)

    context("뿌리기 마감임박 조회") {
        test("성공") {
            //given
            every { sprinkleRepository.findAllByDeadLine(10, 4) } returns sprinkleListVos
            every { participantRepository.findAllSprinkleIdByMemberId(userInfoDto.id) } returns listOf(1L)

            //when
            val resDtos = sprinkleService.getSprinkles(userInfoDto, OrderBy.DEADLINE, Category.ALL, NoOffsetRequest.of())

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
                sprinkleService.getSprinkles(userInfoDto, OrderBy.DEADLINE, Category.CAFE, NoOffsetRequest.of())
            }
        }
    }

    context("뿌리기 카테고리별 조회") {
        test("전체 조회 성공") {
            //given
            val category = Category.ALL
            val noOffsetRequest = NoOffsetRequest.of()

            every { sprinkleRepository.findAllByCategory(category, noOffsetRequest) } returns sprinkleListVos
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
            val noOffsetRequest = NoOffsetRequest.of()

            every { sprinkleRepository.findAllByCategory(category, noOffsetRequest) } returns listOf(sprinkleListVos[0])
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
}) {
    companion object {
        private val userInfoDto = UserInfoDto(id = 1, inherenceId = "test-user")

        private val sprinkleListVos = listOf(
            SprinkleListVo(
                sprinkleId = 1,
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                category = Category.CAFE,
                expiredAt = LocalDateTime.now().plusDays(1),
                participants = 3,
                sprinkleAt = LocalDateTime.now().plusMinutes(10)
            ),
            SprinkleListVo(
                sprinkleId = 2,
                brandName = "베스킨라빈스",
                merchandiseName = "파인트",
                category = Category.ICECREAM,
                expiredAt = LocalDateTime.now().plusDays(1),
                participants = 2,
                sprinkleAt = LocalDateTime.now().plusMinutes(9)
            )
        )
    }
}
package mashup.ggiriggiri.gifticonstorm.application.participant

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Coupon
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.participant.Participant
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.participant.vo.ParticipantInfoVo
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.Sprinkle
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime
import java.time.LocalTime

class ParticipantServiceTest : FunSpec({

    val participantRepository = mockk<ParticipantRepository>()
    val sprinkleRepository = mockk<SprinkleRepository>()
    val participantService = ParticipantService(participantRepository, sprinkleRepository)

    context("응모 내역 조회") {
        test("성공") {
            //given
            every { participantRepository.findHistoryByMemberId(userInfoDto.id, noOffsetRequest) } returns participantInfoVos

            //when
            val resDtos = participantService.getParticipantHistory(userInfoDto, noOffsetRequest)

            //then
            resDtos.size shouldBe 1
            resDtos[0].participantId shouldBe 1
            resDtos[0].expiredAt shouldBe now.toLocalDate().toString()
            resDtos[0].participateDate shouldBe now.minusDays(1).toLocalDate().toString()
        }
    }

    context("응모 결과 조회") {
        test("당첨") {
            // given
            val drawResultRequester = Member(id = userInfoDto.id, inherenceId = "user-inherenceId")
            val sprinkleMember = Member(id = 2, inherenceId = "user-inherenceId")
            val participant = Member(id = 3, inherenceId = "user-inherenceId")
            val sprinkle = Sprinkle(
                member = sprinkleMember,
                coupon = Coupon(brandName = "베스킨라빈스", merchandiseName = "싱글 레귤러", expiredAt = LocalDateTime.now().plusMonths(2), imageUrl = "http://dummyimage.com/240x100.png/dddddd/000000", category = Category.ICECREAM, member = sprinkleMember),
                sprinkleAt = LocalDateTime.now().plusHours(2),
                sprinkled = false
            )

            sprinkle.participants.add(Participant(member = drawResultRequester, drawStatus = DrawStatus.WIN, sprinkle = sprinkle))
            sprinkle.participants.add(Participant(member = participant, drawStatus = DrawStatus.LOSE, sprinkle = sprinkle))

            every { sprinkleRepository.findByIdOrNull(sprinkleId) } returns sprinkle

            // when
            val drawResultResDto = participantService.getDrawResult(userInfoDto, sprinkleId)

            // then
            drawResultResDto.drawStatus shouldBe DrawStatus.WIN
        }

    }
}) {
    companion object {

        val userInfoDto = UserInfoDto(id = 1, inherenceId = "test-user")
        val noOffsetRequest = NoOffsetRequest.of()

        val now: LocalDateTime = LocalDateTime.now()

        const val sprinkleId = 1L

        val participantInfoVos = listOf(
            ParticipantInfoVo(
                participantId = 1,
                sprinkleId = 1,
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                expiredAt = now.with(LocalTime.MAX),
                category = Category.CAFE,
                participants = 100,
                createdAt = now.minusDays(1),
                isChecked = false,
                drawStatus = DrawStatus.WIN
            )
        )
    }
}
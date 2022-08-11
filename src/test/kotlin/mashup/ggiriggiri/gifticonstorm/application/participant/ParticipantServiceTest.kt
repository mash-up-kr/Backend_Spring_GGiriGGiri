package mashup.ggiriggiri.gifticonstorm.application.participant

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import mashup.ggiriggiri.gifticonstorm.application.push.DrawStatus
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.participant.vo.ParticipantInfoVo
import java.time.LocalDateTime
import java.time.LocalTime

class ParticipantServiceTest : FunSpec({

    val participantRepository = mockk<ParticipantRepository>()
    val participantService = ParticipantService(participantRepository)

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
}) {
    companion object {

        val userInfoDto = UserInfoDto(id = 1, inherenceId = "test-user")
        val noOffsetRequest = NoOffsetRequest.of()

        val now: LocalDateTime = LocalDateTime.now()

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
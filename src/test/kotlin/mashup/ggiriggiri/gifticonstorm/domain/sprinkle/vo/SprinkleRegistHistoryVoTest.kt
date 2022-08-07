package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.SprinkleRegistHistoryResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.SprinkledStatus
import java.time.LocalDateTime

class SprinkleRegistHistoryVoTest : FunSpec({

    val now = LocalDateTime.now()

    context("응모 진행 여부") {
        test("뿌리기 진행중이면 PROGRESS 반환") {
            val vo = SprinkleRegistHistoryVo(
                sprinkleId = 1,
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                expiredAt = now.plusDays(1),
                category = Category.CAFE,
                participants = 100,
                sprinkled = false,
                sprinkleAt = now.plusMinutes(10)
            )
            SprinkleRegistHistoryResDto.of(vo).sprinkledStatus shouldBe SprinkledStatus.PROGRESS
        }

        test("뿌리기 종료 & 참여자 수 1명 이상이면 FINISH 반환") {
            val vo = SprinkleRegistHistoryVo(
                sprinkleId = 1,
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                expiredAt = now.plusDays(1),
                category = Category.CAFE,
                participants = 100,
                sprinkled = true,
                sprinkleAt = now.minusMinutes(10)
            )
            SprinkleRegistHistoryResDto.of(vo).sprinkledStatus shouldBe SprinkledStatus.FINISH
        }

        test("뿌리기 종료 & 참여자 수 0명이면 NO_PARTICIPANTS 반환") {
            val vo = SprinkleRegistHistoryVo(
                sprinkleId = 1,
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                expiredAt = now.plusDays(1),
                category = Category.CAFE,
                participants = 0,
                sprinkled = true,
                sprinkleAt = now.minusMinutes(10)
            )
            SprinkleRegistHistoryResDto.of(vo).sprinkledStatus shouldBe SprinkledStatus.NO_PARTICIPANTS
        }
    }
})
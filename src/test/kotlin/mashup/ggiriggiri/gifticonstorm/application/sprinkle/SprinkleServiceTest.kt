package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleListVo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class SprinkleServiceTest {

    @Mock
    private lateinit var sprinkleRepository: SprinkleRepository

    @Mock
    private lateinit var participantRepository: ParticipantRepository

    @Mock
    private lateinit var sprinkleCache: SprinkleCache

    @InjectMocks
    private lateinit var sprinkleService: SprinkleService

    @Test
    fun `뿌리기 마감임박 조회 성공`() {
        //given
        val sprinkleListVos = listOf(
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
        Mockito.`when`(sprinkleRepository.findAllByDeadLine(10, 4)).thenReturn(sprinkleListVos)

        val sprinkleIds = listOf(1L)
        Mockito.`when`(participantRepository.findAllSprinkleIdByMemberId(1)).thenReturn(sprinkleIds)

        //when
        val resDtos = sprinkleService.getSprinkles(OrderBy.DEADLINE, Category.ALL, NoOffsetRequest.of())

        //then
        assertThat(resDtos.size).isEqualTo(2)
        assertThat(resDtos[0].sprinkleId).isEqualTo(sprinkleListVos[0].sprinkleId)
        assertThat(resDtos[0].brandName).isEqualTo(sprinkleListVos[0].brandName)
        assertThat(resDtos[0].participateIn).isEqualTo(true)
        assertThat(resDtos[1].sprinkleId).isEqualTo(sprinkleListVos[1].sprinkleId)
        assertThat(resDtos[1].brandName).isEqualTo(sprinkleListVos[1].brandName)
        assertThat(resDtos[1].participateIn).isEqualTo(false)
    }

    @Test
    fun `뿌리기 전체 조회 성공`() {
        //given
        val category = Category.ALL
        val noOffsetRequest = NoOffsetRequest.of()

        val sprinkleListVos = listOf(
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
        Mockito.`when`(sprinkleRepository.findAllByCategory(category, noOffsetRequest)).thenReturn(sprinkleListVos)

        val sprinkleIds = listOf(1L)
        Mockito.`when`(participantRepository.findAllSprinkleIdByMemberId(1)).thenReturn(sprinkleIds)

        //when
        val resDtos = sprinkleService.getSprinkles(OrderBy.CREATED_AT, category, noOffsetRequest)

        //then
        assertThat(resDtos.size).isEqualTo(2)
        assertThat(resDtos[0].sprinkleId).isEqualTo(sprinkleListVos[0].sprinkleId)
        assertThat(resDtos[0].brandName).isEqualTo(sprinkleListVos[0].brandName)
        assertThat(resDtos[0].participateIn).isEqualTo(true)
        assertThat(resDtos[1].sprinkleId).isEqualTo(sprinkleListVos[1].sprinkleId)
        assertThat(resDtos[1].brandName).isEqualTo(sprinkleListVos[1].brandName)
        assertThat(resDtos[1].participateIn).isEqualTo(false)
    }

    @Test
    fun `뿌리기 카테고리별 조회 성공`() {
        //given
        val category = Category.CAFE
        val noOffsetRequest = NoOffsetRequest.of()

        val sprinkleListVos = listOf(
            SprinkleListVo(
                sprinkleId = 1,
                brandName = "스타벅스",
                merchandiseName = "아이스 아메리카노",
                category = Category.CAFE,
                expiredAt = LocalDateTime.now().plusDays(1),
                participants = 3,
                sprinkleAt = LocalDateTime.now().plusMinutes(10)
            )
        )
        Mockito.`when`(sprinkleRepository.findAllByCategory(category, noOffsetRequest)).thenReturn(sprinkleListVos)

        val sprinkleIds = listOf(1L)
        Mockito.`when`(participantRepository.findAllSprinkleIdByMemberId(1)).thenReturn(sprinkleIds)

        //when
        val resDtos = sprinkleService.getSprinkles(OrderBy.CREATED_AT, category, noOffsetRequest)

        //then
        assertThat(resDtos.size).isEqualTo(1)
        assertThat(resDtos[0].sprinkleId).isEqualTo(sprinkleListVos[0].sprinkleId)
        assertThat(resDtos[0].brandName).isEqualTo(sprinkleListVos[0].brandName)
        assertThat(resDtos[0].participateIn).isEqualTo(true)
    }

    @Test
    fun `뿌리기 조회 실패 - orderBy=null, category=null 일 때`() {
        assertThrows<BaseException> {
            sprinkleService.getSprinkles(null, null, NoOffsetRequest.of())
        }
    }

    @Test
    fun `뿌리기 조회 실패 - orderBy=DEADLINE, category!=ALL 일 때`() {
        assertThrows<BaseException> {
            sprinkleService.getSprinkles(OrderBy.DEADLINE, Category.CAFE, NoOffsetRequest.of())
        }
    }

}
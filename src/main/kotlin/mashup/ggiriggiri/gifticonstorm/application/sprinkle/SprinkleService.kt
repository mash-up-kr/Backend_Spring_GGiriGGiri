package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.participant.repository.ParticipantRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.OrderBy
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.dto.GetSprinkleResDto
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository.SprinkleRepository
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleListVo
import org.springframework.stereotype.Service

@Service
class SprinkleService(
    private val sprinkleCache: SprinkleCache,
    private val sprinkleRepository: SprinkleRepository,
    private val participantRepository: ParticipantRepository
) {
    fun getSprinkles(orderBy: OrderBy?, category: Category?): List<GetSprinkleResDto> {
        if (orderBy == null || category == null) {
            throw BaseException(ResponseCode.INVALID_INPUT_VALUE)
        }
        val sprinkleListVos = if (orderBy == OrderBy.DEADLINE) findAllByDeadLine(category) else findAllByCategory(category)
        val sprinkleIds = participantRepository.findAllSprinkleIdByMemberId(1) //TODO: 로그인 기능 추가 후 해당 사용자 id 전달
        return sprinkleListVos.map {
            GetSprinkleResDto(
                brandName = it.brandName,
                merchandiseName = it.merchandiseName,
                category = it.category,
                expiredAt = it.expiredAt.toString(),
                participants = it.participants,
                sprinkleAt = it.sprinkleAt.toString(),
                participateIn = it.sprinkleId in sprinkleIds
            )
        }
    }

    private fun findAllByCategory(category: Category): List<SprinkleListVo> {
        return sprinkleRepository.findAllByCategory(category)
    }

    private fun findAllByDeadLine(category: Category): List<SprinkleListVo> {
        if (category != Category.ALL) throw BaseException(ResponseCode.INVALID_INPUT_VALUE)
        return sprinkleRepository.findAllByDeadLine(10, 4)
    }

}
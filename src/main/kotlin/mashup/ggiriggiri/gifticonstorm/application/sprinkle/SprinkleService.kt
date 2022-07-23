package mashup.ggiriggiri.gifticonstorm.application.sprinkle

import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.common.dto.ResponseCode
import mashup.ggiriggiri.gifticonstorm.common.error.exception.BaseException
import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoDto
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
    fun getSprinkles(
        userInfoDto: UserInfoDto, orderBy: OrderBy?, category: Category?, noOffsetRequest: NoOffsetRequest
    ): List<GetSprinkleResDto> {
        if (orderBy == null || category == null) {
            throw BaseException(ResponseCode.INVALID_INPUT_VALUE)
        }
        val sprinkleListVos =
            if (orderBy == OrderBy.DEADLINE) findAllByDeadLine(category)
            else findAllByCategory(category, noOffsetRequest)
        val sprinkleIds = participantRepository.findAllSprinkleIdByMemberId(userInfoDto.id)
        return sprinkleListVos.map { GetSprinkleResDto.toDto(it, sprinkleIds) }
    }

    private fun findAllByCategory(category: Category, noOffsetRequest: NoOffsetRequest): List<SprinkleListVo> {
        return sprinkleRepository.findAllByCategory(category, noOffsetRequest)
    }

    private fun findAllByDeadLine(category: Category): List<SprinkleListVo> {
        if (category != Category.ALL) throw BaseException(ResponseCode.INVALID_INPUT_VALUE)
        return sprinkleRepository.findAllByDeadLine(10, 4)
    }

}
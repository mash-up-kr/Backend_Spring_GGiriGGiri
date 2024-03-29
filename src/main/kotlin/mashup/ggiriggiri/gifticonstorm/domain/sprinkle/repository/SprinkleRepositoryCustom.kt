package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository

import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.GetSprinkleVo
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleInfoVo
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleRegistHistoryVo

interface SprinkleRepositoryCustom {

    fun findAllByDeadLine(leftMinute: Long, limit: Long): List<GetSprinkleVo>
    fun findAllByCategory(category: Category, noOffsetRequest: NoOffsetRequest): List<GetSprinkleVo>

    fun findInfoById(id: Long): SprinkleInfoVo?

    fun findRegistHistoryByMemberId(memberId: Long, noOffsetRequest: NoOffsetRequest): List<SprinkleRegistHistoryVo>
}
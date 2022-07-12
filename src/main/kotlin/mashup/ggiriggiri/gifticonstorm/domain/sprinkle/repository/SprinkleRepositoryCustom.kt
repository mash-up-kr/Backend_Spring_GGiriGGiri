package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository

import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleListVo

interface SprinkleRepositoryCustom {

    fun findAllByDeadLine(leftMinute: Long, limit: Long): List<SprinkleListVo>
    fun findAllByCategory(category: Category): List<SprinkleListVo>
}
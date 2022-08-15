package mashup.ggiriggiri.gifticonstorm.domain.sprinkle.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.Category
import mashup.ggiriggiri.gifticonstorm.domain.coupon.domain.QCoupon.coupon
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.domain.QSprinkle.sprinkle
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.QSprinkleInfoVo
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.QSprinkleRegistHistoryVo
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleInfoVo
import mashup.ggiriggiri.gifticonstorm.domain.sprinkle.vo.SprinkleRegistHistoryVo
import java.time.LocalDateTime
import java.util.stream.Collectors

class SprinkleRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory
) : SprinkleRepositoryCustom {

    //뿌리기 남은 시간 {leftMinute} 이내 & 참여자 수 많은 것 중 상위 {limit}개
    override fun findAllByDeadLine(leftMinute: Long, limit: Long): List<SprinkleInfoVo> {
        return jpaQueryFactory
            .select(QSprinkleInfoVo(
                sprinkle.id,
                coupon.brandName,
                coupon.merchandiseName,
                coupon.category,
                coupon.expiredAt,
                sprinkle.participants.size(),
                sprinkle.sprinkleAt,
                sprinkle.member.id
            ))
            .from(sprinkle)
            .join(sprinkle.coupon, coupon)
            .where(withinTime(leftMinute))
            .fetch().stream().sorted(
                Comparator.comparingInt(SprinkleInfoVo::participants).reversed()
            ).limit(limit).collect(Collectors.toList())
    }

    override fun findAllByCategory(category: Category, noOffsetRequest: NoOffsetRequest): List<SprinkleInfoVo> {
        return jpaQueryFactory
            .select(QSprinkleInfoVo(
                sprinkle.id,
                coupon.brandName,
                coupon.merchandiseName,
                coupon.category,
                coupon.expiredAt,
                sprinkle.participants.size(),
                sprinkle.sprinkleAt,
                sprinkle.member.id
            ))
            .from(sprinkle)
            .join(sprinkle.coupon, coupon)
            .where(
                gtSprinkleId(noOffsetRequest.id),
                sprinkle.sprinkled.isFalse,
                eqCategory(category)
            )
            .orderBy(sprinkle.id.asc())
            .limit(noOffsetRequest.limit)
            .fetch()
    }

    override fun findInfoById(id: Long): SprinkleInfoVo? {
        return jpaQueryFactory
            .select(QSprinkleInfoVo(
                sprinkle.id,
                coupon.brandName,
                coupon.merchandiseName,
                coupon.category,
                coupon.expiredAt,
                sprinkle.participants.size(),
                sprinkle.sprinkleAt,
                sprinkle.member.id
            ))
            .from(sprinkle)
            .join(sprinkle.coupon, coupon)
            .where(sprinkle.id.eq(id))
            .fetchOne()
    }

    override fun findRegistHistoryByMemberId(memberId: Long, noOffsetRequest: NoOffsetRequest): List<SprinkleRegistHistoryVo> {
        return jpaQueryFactory
            .select(QSprinkleRegistHistoryVo(
                sprinkle.id,
                coupon.brandName,
                coupon.merchandiseName,
                coupon.expiredAt,
                coupon.category,
                sprinkle.participants.size(),
                sprinkle.sprinkled,
                sprinkle.sprinkleAt
            ))
            .from(sprinkle)
            .join(sprinkle.coupon, coupon)
            .where(
                ltSprinkleId(noOffsetRequest.id),
                sprinkle.member.id.eq(memberId)
            )
            .orderBy(sprinkle.id.desc())
            .limit(noOffsetRequest.limit)
            .fetch()
    }

    private fun ltSprinkleId(id: Long?): BooleanExpression? {
        return id?.let { sprinkle.id.lt(it) }
    }

    private fun gtSprinkleId(id: Long?): BooleanExpression? {
        return id?.let { sprinkle.id.gt(it) }
    }

    private fun eqCategory(category: Category): BooleanExpression? {
        return if (category == Category.ALL) null else coupon.category.eq(category)
    }

    private fun withinTime(leftMinute: Long): BooleanExpression {
        val now = LocalDateTime.now()
        return sprinkle.sprinkleAt.between(now, now.plusMinutes(leftMinute))
    }
}
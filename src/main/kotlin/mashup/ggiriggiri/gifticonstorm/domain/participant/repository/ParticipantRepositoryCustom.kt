package mashup.ggiriggiri.gifticonstorm.domain.participant.repository

import mashup.ggiriggiri.gifticonstorm.common.dto.NoOffsetRequest
import mashup.ggiriggiri.gifticonstorm.domain.participant.vo.ParticipantInfoVo

interface ParticipantRepositoryCustom {

    fun findAllSprinkleIdByMemberId(memberId: Long): List<Long>

    fun findHistoryByMemberId(memberId: Long, noOffsetRequest: NoOffsetRequest): List<ParticipantInfoVo>

    fun findAllMemberIdBySprinkleId(sprinkleId: Long): List<Long>
}
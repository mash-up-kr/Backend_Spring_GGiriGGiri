package mashup.ggiriggiri.gifticonstorm.domain.member.repository

import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long> {
    fun findByInherenceId(inherenceId: String): Member?
}

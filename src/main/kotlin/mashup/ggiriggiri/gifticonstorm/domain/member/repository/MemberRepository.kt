package mashup.ggiriggiri.gifticonstorm.domain.member.repository

import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import org.springframework.boot.autoconfigure.AutoConfigureOrder
import org.springframework.core.annotation.Order
import org.springframework.data.jpa.repository.JpaRepository

@Order(0)
interface MemberRepository : JpaRepository<Member, Long> {
    fun findByInherenceId(inherenceId: String): Member?
}

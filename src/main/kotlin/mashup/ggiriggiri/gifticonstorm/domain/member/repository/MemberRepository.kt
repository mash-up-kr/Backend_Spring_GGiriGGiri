package mashup.ggiriggiri.gifticonstorm.domain.member.repository

import mashup.ggiriggiri.gifticonstorm.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository: JpaRepository<Member, Long>
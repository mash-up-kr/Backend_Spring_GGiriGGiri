package mashup.ggiriggiri.gifticonstorm.config.resolver

import mashup.ggiriggiri.gifticonstorm.common.error.exception.UnauthorizedException
import mashup.ggiriggiri.gifticonstorm.config.annotation.UserInfo
import mashup.ggiriggiri.gifticonstorm.domain.member.domain.Member
import mashup.ggiriggiri.gifticonstorm.domain.member.repository.MemberRepository
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class UserInfoResolver(private val memberRepository: MemberRepository) : HandlerMethodArgumentResolver {
    companion object {
        const val AUTHORIZATION_KEY_HEADER_NAME = "Authorization"
    }

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(UserInfo::class.java) && parameter.parameter.type == UserInfoDto::class.java
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?
    ): Any? {
        val authkey = webRequest.getHeader(AUTHORIZATION_KEY_HEADER_NAME) ?: throw UnauthorizedException()
        val member = memberRepository.findByInherenceId(authkey) ?: memberRepository.save(Member(inherenceId = authkey))
        return UserInfoDto(id = member.id, inherenceId = member.inherenceId)
    }
}

data class UserInfoDto(
    val id: Long,
    val inherenceId: String
)
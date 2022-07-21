package mashup.ggiriggiri.gifticonstorm.config

import mashup.ggiriggiri.gifticonstorm.config.resolver.UserInfoResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(private val userInfoResolver: UserInfoResolver) : WebMvcConfigurer {

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(userInfoResolver)
    }
}
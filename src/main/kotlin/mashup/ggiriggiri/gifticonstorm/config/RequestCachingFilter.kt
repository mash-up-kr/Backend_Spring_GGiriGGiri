package mashup.ggiriggiri.gifticonstorm.config

import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class RequestCachingFilter : OncePerRequestFilter() {
    companion object : Logger
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val cachedRequest = request.wrapCache()
        val cachedResponse = response.wrapCache()
        filterChain.doFilter(cachedRequest, cachedResponse)
        cachedResponse.copyBodyToResponse()
    }
}

fun HttpServletRequest.wrapCache(): ContentCachingRequestWrapper {
    if (this !is ContentCachingRequestWrapper)
        return ContentCachingRequestWrapper(this)
    return this
}

fun HttpServletResponse.wrapCache(): ContentCachingResponseWrapper {
    if (this !is ContentCachingResponseWrapper)
        return ContentCachingResponseWrapper(this)
    return this
}
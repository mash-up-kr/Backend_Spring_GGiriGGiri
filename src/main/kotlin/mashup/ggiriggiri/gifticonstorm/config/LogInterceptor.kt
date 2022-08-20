package mashup.ggiriggiri.gifticonstorm.config

import mashup.ggiriggiri.gifticonstorm.common.DEFAULT_OBJECT_MAPPER
import mashup.ggiriggiri.gifticonstorm.infrastructure.Logger
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.util.ContentCachingRequestWrapper
import org.springframework.web.util.ContentCachingResponseWrapper
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LogInterceptor : HandlerInterceptor {

    companion object : Logger {
        const val LOG_IGNORE_REGEX = "(/health)"
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {

        val cachedRequest = request.wrapCache()
        val cachedResponse = response.wrapCache()

        runCatching {
            if (!Regex(LOG_IGNORE_REGEX).containsMatchIn(request.requestURI)) {
                val reqBody = makeBody(request.contentType, cachedRequest.contentAsByteArray)
                val resBody = makeBody(response.contentType, cachedResponse.contentAsByteArray)
                log.info(makeLog(cachedRequest, cachedResponse, reqBody, resBody))
            }
        }.onFailure {
            log.error("print log error")
        }
    }

    private fun makeLog(cachedRequest: ContentCachingRequestWrapper, cachedResponse: ContentCachingResponseWrapper, reqBody: String, resBody: String): String {
        val logBuilder = StringBuilder()
        logBuilder.append("\n")
        logBuilder.append("===========================IN===========================").append("\n")
        logBuilder.append("uri:${cachedRequest.requestURI}").append("\n")
        logBuilder.append("reqBody:$reqBody").append("\n")
        logBuilder.append("reqParam:${cachedRequest.queryString}").append("\n")
        logBuilder.append("===========================OUT==========================").append("\n")
        logBuilder.append("responseStatus:${cachedResponse.status}").append("\n")
        logBuilder.append("resBody:$resBody").append("\n")

        return logBuilder.toString()
    }

    private fun makeBody(mediaType: String?, contentAsByteArray: ByteArray): String {
        return when (mediaType) {
            MediaType.APPLICATION_JSON_VALUE -> {
                runCatching {
                    DEFAULT_OBJECT_MAPPER.readTree(contentAsByteArray)
                }.fold({
                    it.toPrettyString()
                },{
                    it.message
                }).toString()
            }
            else -> String(contentAsByteArray)
        }
    }

}


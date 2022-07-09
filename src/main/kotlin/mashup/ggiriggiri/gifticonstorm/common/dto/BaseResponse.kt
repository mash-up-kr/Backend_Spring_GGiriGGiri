package mashup.ggiriggiri.gifticonstorm.common.dto

class BaseResponse<T>(
    val code: String,
    val message: String,
    val data: T? = null
) {

    companion object {
        fun ok(): BaseResponse<Unit> {
            return BaseResponse(
                code = ResponseCode.OK.code,
                message = ResponseCode.OK.message
            )
        }

        fun <T> ok(data: T): BaseResponse<T> {
            return BaseResponse(
                code = ResponseCode.OK.code,
                message = ResponseCode.OK.message,
                data = data
            )
        }

        fun error(responseCode: ResponseCode): BaseResponse<Unit> {
            return BaseResponse(
                code = responseCode.code,
                message = responseCode.message
            )
        }

        fun <T> error(responseCode: ResponseCode, data: T): BaseResponse<T> {
            return BaseResponse(
                code = responseCode.code,
                message = responseCode.message,
                data = data
            )
        }
    }
}
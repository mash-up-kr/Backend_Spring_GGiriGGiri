package mashup.ggiriggiri.gifticonstorm.common.dto

import mashup.ggiriggiri.gifticonstorm.common.error.ErrorCode

class BaseResponse<T>(
    val success: Boolean,
    val code: String,
    val message: String,
    val data: T? = null
) {

    companion object {
        fun ok(): BaseResponse<Unit> {
            return BaseResponse(
                success = true,
                code = "0",
                message = "성공하였습니다."
            )
        }

        fun <T> ok(data: T): BaseResponse<T> {
            return BaseResponse(
                success = true,
                code = "0",
                message = "성공하였습니다.",
                data = data
            )
        }

        fun error(errorCode: ErrorCode): BaseResponse<Unit> {
            return BaseResponse(
                success = false,
                code = errorCode.code,
                message = errorCode.message
            )
        }

        fun <T> error(errorCode: ErrorCode, data: T): BaseResponse<T> {
            return BaseResponse(
                success = false,
                code = errorCode.code,
                message = errorCode.message,
                data = data
            )
        }
    }
}
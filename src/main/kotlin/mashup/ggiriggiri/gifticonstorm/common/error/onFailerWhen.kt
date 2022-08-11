package mashup.ggiriggiri.gifticonstorm.common.error


fun <T> Result<T>.onFailureWhen(vararg exception: Class<out Throwable>, action: () -> Unit) : Result<T> {
    return this.onFailure {
        if (exception.contains(it.javaClass)) {
            throw it
        }

        action()
    }

}
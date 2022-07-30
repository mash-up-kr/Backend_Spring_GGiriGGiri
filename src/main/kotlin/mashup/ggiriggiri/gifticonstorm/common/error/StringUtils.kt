package mashup.ggiriggiri.gifticonstorm.common.error

class StringUtils {

    companion object {
        fun removeNewLine(s : String): String {
            return s.replace("\n", "")
        }
    }

}
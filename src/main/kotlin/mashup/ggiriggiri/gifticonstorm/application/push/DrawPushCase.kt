package mashup.ggiriggiri.gifticonstorm.application.push

enum class DrawPushCase(val pushMessage: String) {
    WIN("당첨 되셨습니다."),
    LOSE("낙첨 되셨습니다.")
}
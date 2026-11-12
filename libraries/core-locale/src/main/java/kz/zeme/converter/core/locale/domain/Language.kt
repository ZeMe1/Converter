package kz.zeme.converter.core.locale.domain

data class Language(
    val isSelected: Boolean,
    val code: LanguageCode,
) {

    enum class LanguageCode(
        val code: String,
    ) {
        Kz("kk"),
        Ru("ru"),
        En("en");

        companion object {

            fun find(
                code: String?,
                default: LanguageCode = Ru,
            ): LanguageCode = entries
                .find { it.code.contentEquals(code) }
                ?: default
        }
    }
}

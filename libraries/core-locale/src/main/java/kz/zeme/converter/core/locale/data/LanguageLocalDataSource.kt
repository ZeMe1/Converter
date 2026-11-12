package kz.zeme.converter.core.locale.data

import android.content.SharedPreferences
import androidx.core.content.edit
import kz.zeme.converter.core.locale.domain.Language.LanguageCode

class LanguageLocalDataSource(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val LANGUAGE_CODE_KEY = "LANGUAGE_CODE_KEY"
    }

    fun getSelectedLanguageCode(defaultCode: LanguageCode): LanguageCode =
        sharedPreferences.getString(LANGUAGE_CODE_KEY, defaultCode.code)
            ?.let { LanguageCode.find(code = it, default = defaultCode) }
            ?: defaultCode

    fun saveSelectedLanguageCode(languageCode: LanguageCode?) {
        languageCode ?: return

        sharedPreferences.edit(commit = true) {
            putString(LANGUAGE_CODE_KEY, languageCode.code)
        }
    }
}

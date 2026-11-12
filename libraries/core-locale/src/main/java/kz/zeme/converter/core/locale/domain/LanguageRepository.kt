package kz.zeme.converter.core.locale.domain

import kz.zeme.converter.core.domain.Response

interface LanguageRepository {

    suspend fun getLanguages(): Response<List<Language>>

    suspend fun saveSelectedLanguage(language: Language): Response<Unit>
}

package kz.zeme.converter.core.locale.domain

import java.util.Locale

interface LocaleRepository {

    fun getSelectedLocale(): Locale
}

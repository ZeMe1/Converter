package kz.zeme.converter.core.locale.di

import android.content.Context
import android.content.SharedPreferences
import kz.zeme.converter.core.di.extensions.binds
import kz.zeme.converter.core.locale.data.DefaultLanguageRepository
import kz.zeme.converter.core.locale.data.LanguageLocalDataSource
import kz.zeme.converter.core.locale.domain.LanguageRepository
import kz.zeme.converter.core.locale.domain.LocaleRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private const val LOCALE_PREFERENCES_NAME = "locale_preferences"

val coreLocaleModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences(LOCALE_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    single {
        LanguageLocalDataSource(
            sharedPreferences = get(),
        )
    }

    single {
        DefaultLanguageRepository(
            activityProvider = get(),
            languageLocalDataSource = get(),
        )
    }.binds(
        LanguageRepository::class,
        LocaleRepository::class,
    )
}

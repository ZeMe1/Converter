package kz.zeme.converter.di

import android.app.Application.ActivityLifecycleCallbacks
import kz.zeme.converter.core.activity.ActivityProvider
import kz.zeme.converter.core.di.extensions.binds
import kz.zeme.converter.core.navigation.ExternalModulesNavigator
import kz.zeme.converter.features.AppGlobalActivityProvider
import kz.zeme.converter.features.main.MainActivityVM
import kz.zeme.converter.features.navigation.AppGlobalNavigator
import kz.zeme.converter.features.navigation.deeplink.AppGlobalRedirectParser
import kz.zeme.converter.features.navigation.deeplink.AppRedirectParser
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        AppGlobalActivityProvider()
    }.binds(
        ActivityProvider::class,
        ActivityLifecycleCallbacks::class,
    )

    factory {
        AppGlobalRedirectParser()
    }.binds(
        AppRedirectParser::class,
    )

    viewModel {
        MainActivityVM(
            appRedirectParser = get(),
        )
    }

    single {
        AppGlobalNavigator(
            activityProvider = get(),
            languagePickerRouter = get(),
            currencyPickerRouter = get(),
        )
    }.binds(
        ExternalModulesNavigator::class,
    )
}

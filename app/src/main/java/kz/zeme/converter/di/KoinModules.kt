package kz.zeme.converter.di

import kz.zeme.converter.core.locale.di.coreLocaleModule
import kz.zeme.converter.core.network.di.coreNetworkModule
import kz.zeme.converter.core.ui.di.coreUiModule
import org.koin.core.module.Module

open class KoinModules {

    open fun getModules(): List<Module> = listOf(
        coreNetworkModule,
        coreUiModule,
        coreLocaleModule,
        appModule,
    )
}

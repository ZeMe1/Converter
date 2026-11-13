package kz.zeme.converter.core.navigation.di

import kz.zeme.converter.core.navigation.NavigationResultLiveData
import org.koin.dsl.module

val coreNavigationModule = module {

    single { NavigationResultLiveData() }
}

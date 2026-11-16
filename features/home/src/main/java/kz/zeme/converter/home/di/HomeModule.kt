package kz.zeme.converter.home.di

import kz.zeme.converter.home.presentation.HomeVM
import kz.zeme.converter.home.presentation.navigation.HomeRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {

    factory { HomeRouter() }

    viewModel {
        HomeVM(
            currencyRepository = get(),
            currencyFormatter = get(),
            navigationResult = get(),
        )
    }
}

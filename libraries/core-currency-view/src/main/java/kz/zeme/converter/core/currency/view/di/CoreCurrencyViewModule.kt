package kz.zeme.converter.core.currency.view.di

import kz.zeme.converter.core.currency.view.presentation.CurrencyPickerVM
import kz.zeme.converter.core.currency.view.presentation.navigation.CurrencyPickerRouter
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val coreCurrencyViewModule = module {

    factory { CurrencyPickerRouter() }

    viewModel { params ->
        CurrencyPickerVM(
            baseCode = params.get(),
            selectedCode = params.get(),
            requestCode = params.get(),
            currencyRepository = get(),
            navigationResult = get(),
        )
    }
}

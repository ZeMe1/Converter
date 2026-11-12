package kz.zeme.converter.core.currency.di

import kz.zeme.converter.core.currency.data.CurrencyApiMapper
import kz.zeme.converter.core.currency.data.CurrencyNetworkDataSource
import kz.zeme.converter.core.currency.data.CurrencyRateApiMapper
import kz.zeme.converter.core.currency.data.DefaultCurrencyRepository
import kz.zeme.converter.core.currency.domain.CurrencyRepository
import kz.zeme.converter.core.di.extensions.binds
import kz.zeme.converter.core.env.props.BaseSettings.Property
import kz.zeme.converter.core.network.extensions.createWebService
import org.koin.dsl.module

val coreCurrencyModule = module {

    single {
        createWebService<CurrencyNetworkDataSource>(Property.baseUrl)
    }

    factory { CurrencyApiMapper() }
    factory { CurrencyRateApiMapper() }

    single {
        DefaultCurrencyRepository(
            networkDataSource = get(),
            currencyApiMapper = get(),
            currencyRateApiMapper = get(),
            gson = get(),
        )
    }.binds(
        CurrencyRepository::class,
    )
}

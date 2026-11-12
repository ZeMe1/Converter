package kz.zeme.converter.core.currency.data

import com.google.gson.Gson
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kz.zeme.converter.core.currency.domain.CurrencyRates
import kz.zeme.converter.core.currency.domain.CurrencyRepository
import kz.zeme.converter.core.domain.Response
import kz.zeme.converter.core.network.extensions.mapFailure
import kz.zeme.converter.core.network.extensions.tryMapSuspended

class DefaultCurrencyRepository(
    private val networkDataSource: CurrencyNetworkDataSource,
    private val currencyApiMapper: CurrencyApiMapper,
    private val currencyRateApiMapper: CurrencyRateApiMapper,
    private val gson: Gson,
) : CurrencyRepository {

    override suspend fun getRates(
        baseCode: String,
        query: String,
        excludeBase: Boolean,
    ): Response<CurrencyRates> = coroutineScope {

        val currenciesDeferred = async {
            tryMapSuspended(
                request = networkDataSource::getCurrencies,
                mapSuccess = currencyApiMapper::map,
                mapFailure = gson::mapFailure,
            )
        }
        val ratesDeferred = async {
            tryMapSuspended(
                request = { networkDataSource.getRates(base = baseCode) },
                mapSuccess = { it },
                mapFailure = gson::mapFailure,
            )
        }

        val currencies = when (val response = currenciesDeferred.await()) {
            is Response.Failure -> {
                ratesDeferred.cancel()
                return@coroutineScope Response.Failure(response.data)
            }
            is Response.Success -> response.data
        }

        val rates = when (val response = ratesDeferred.await()) {
            is Response.Failure -> return@coroutineScope Response.Failure(response.data)
            is Response.Success -> response.data
        }

        Response.Success(
            currencyRateApiMapper.map(
                baseCode = baseCode,
                rates = rates,
                currencies = currencies,
                query = query,
                excludeBase = excludeBase,
            )
        )
    }
}

package kz.zeme.converter.core.currency.domain

import kz.zeme.converter.core.domain.Response

interface CurrencyRepository {

    suspend fun getRates(
        baseCode: String,
        query: String = "",
        excludeBase: Boolean = false,
    ): Response<CurrencyRates>
}

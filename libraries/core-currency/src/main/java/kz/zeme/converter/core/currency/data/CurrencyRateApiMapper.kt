package kz.zeme.converter.core.currency.data

import kotlinx.collections.immutable.toImmutableList
import kz.zeme.converter.core.currency.domain.CurrencyRate
import kz.zeme.converter.core.currency.domain.CurrencyRates

class CurrencyRateApiMapper {

    fun map(
        baseCode: String,
        rates: List<CurrencyRateApi>,
        currencies: Map<String, String>,
        query: String = "",
        excludeBase: Boolean = false,
    ): CurrencyRates {
        val mappedRates = rates
            .mapNotNull { rateApi ->
                mapOrNull(
                    from = rateApi,
                    currencies = currencies,
                    baseCode = baseCode,
                    excludeBase = excludeBase,
                )
            }
            .filter { item ->
                query.isEmpty()
                || item.code.contains(query, ignoreCase = true)
                || item.name.contains(query, ignoreCase = true)
            }
            .toImmutableList()

        return CurrencyRates(
            baseCode = baseCode,
            baseName = currencies[baseCode].orEmpty(),
            rates = mappedRates,
        )
    }

    private fun mapOrNull(
        from: CurrencyRateApi,
        currencies: Map<String, String>,
        baseCode: String,
        excludeBase: Boolean,
    ): CurrencyRate? {
        val code = from.quote.orEmpty()
        if (excludeBase && code == baseCode) return null

        val name = currencies[code] ?: return null

        return CurrencyRate(
            code = code,
            name = name,
            rate = from.rate ?: 0.0,
        )
    }
}

package kz.zeme.converter.core.currency.domain

import kotlinx.collections.immutable.ImmutableList

data class CurrencyRates(
    val baseCode: String,
    val baseName: String,
    val rates: ImmutableList<CurrencyRate>,
)

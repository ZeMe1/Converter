package kz.zeme.converter.core.currency.domain

data class CurrencyRate(
    val code: String,
    val name: String,
    val rate: Double,
)

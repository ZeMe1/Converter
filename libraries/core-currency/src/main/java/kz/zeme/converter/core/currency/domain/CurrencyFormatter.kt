package kz.zeme.converter.core.currency.domain

interface CurrencyFormatter {

    fun formatAmount(amount: Double): String

    fun formatRate(rate: Double): String
}

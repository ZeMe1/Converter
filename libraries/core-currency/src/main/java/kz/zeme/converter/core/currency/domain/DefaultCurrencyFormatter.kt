package kz.zeme.converter.core.currency.domain

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class DefaultCurrencyFormatter : CurrencyFormatter {

    private val decimalSymbols = DecimalFormatSymbols(Locale.US).apply {
        groupingSeparator = ' '
    }

    private val amountFormat = DecimalFormat("#,##0.00", decimalSymbols)
    private val rateFormatLarge = DecimalFormat("0.00", decimalSymbols)
    private val rateFormatMedium = DecimalFormat("0.####", decimalSymbols)
    private val rateFormatSmall = DecimalFormat("0.#####", decimalSymbols)

    override fun formatAmount(amount: Double): String = amountFormat.format(amount)

    override fun formatRate(rate: Double): String {
        val format = when {
            rate >= 100.0 -> rateFormatLarge
            rate >= 1.0 -> rateFormatMedium
            else -> rateFormatSmall
        }
        return format.format(rate)
    }
}

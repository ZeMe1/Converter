package kz.zeme.converter.home.presentation

interface HomeController {

    fun onStart()
    fun onRefresh()
    fun onAmountChange(amount: String)
    fun onSwapCurrenciesClick()
    fun onOpenLanguagePickerClick()
    fun onPickFromCurrencyClick()
    fun onPickToCurrencyClick()
    fun onOtherRateCurrencyClick(code: String)
}

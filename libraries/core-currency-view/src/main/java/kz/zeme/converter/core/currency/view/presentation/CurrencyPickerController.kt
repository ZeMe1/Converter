package kz.zeme.converter.core.currency.view.presentation

interface CurrencyPickerController {

    fun onStart()
    fun onRefresh()
    fun onQueryUpdate(query: String)
    fun onClearSearch()
    fun onCurrencyClick(item: CurrencyPickerItem)
}

package kz.zeme.converter.core.currency.view.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.converter.core.currency.domain.CurrencyRate
import kz.zeme.converter.core.currency.domain.CurrencyRepository
import kz.zeme.converter.core.domain.RequestCode
import kz.zeme.converter.core.navigation.BaseNavigation
import kz.zeme.converter.core.navigation.NavigationResultLiveData
import kz.zeme.converter.core.navigation.NavigationResultLiveData.NavigationResult.DialogResult
import kz.zeme.converter.core.navigation.Navigator
import kz.zeme.converter.core.observable.EventFlow
import kz.zeme.converter.core.observable.MutableEventFlow
import kz.zeme.converter.core.observable.ViewState
import kz.zeme.converter.core.observable.extensions.getLoadingState
import kz.zeme.converter.core.observable.extensions.getViewState
import kz.zeme.converter.core.observable.extensions.update

class CurrencyPickerVM(
    private val baseCode: String,
    private val selectedCode: String,
    private val requestCode: RequestCode,
    private val currencyRepository: CurrencyRepository,
    private val navigationResult: NavigationResultLiveData,
) : ViewModel(), CurrencyPickerController {

    private val _state = MutableLiveData<ViewState<CurrencyPickerState>>()
    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    private var _loadRatesJob: Job? = null
    private var _allRates: List<CurrencyRate> = emptyList()

    val state: LiveData<ViewState<CurrencyPickerState>> = _state.map { it }
    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    override fun onStart() {
        loadRates()
    }

    override fun onRefresh() {
        loadRates()
    }

    override fun onQueryUpdate(query: String) {
        val filteredItems = filterRates(rates = _allRates, query = query)

        _state.update {
            copy(
                query = query,
                items = filteredItems,
            )
        }
    }

    override fun onClearSearch() = onQueryUpdate("")

    override fun onCurrencyClick(item: CurrencyPickerItem) {
        navigationResult.setResult(
            DialogResult.Ok(
                requestCode = requestCode,
                data = item.code,
            )
        )
        _navigation.value = BaseNavigation.Close
    }

    private fun loadRates() {
        _loadRatesJob?.cancel()

        _loadRatesJob = viewModelScope.launch {
            _state.value = _state.getLoadingState()

            val response = currencyRepository.getRates(
                baseCode = baseCode,
                query = "",
            )

            _state.value = _state.getViewState(
                response = response,
                transform = { currencyRates ->
                    _allRates = currencyRates.rates

                    val currentQuery = _state.value?.data?.query.orEmpty()

                    CurrencyPickerState(
                        query = currentQuery,
                        items = filterRates(rates = _allRates, query = currentQuery),
                    )
                },
            )
        }
    }

    private fun filterRates(
        rates: List<CurrencyRate>,
        query: String,
    ): ImmutableList<CurrencyPickerItem> {
        val normalized = query.trim().lowercase()
        return rates
            .filter { rate ->
                normalized.isBlank()
                || rate.code.lowercase().contains(normalized)
                || rate.name.lowercase().contains(normalized)
            }
            .map { rate ->
                CurrencyPickerItem(
                    code = rate.code,
                    name = rate.name,
                    formattedRate = formatRate(rate.rate),
                    isSelected = rate.code == selectedCode,
                )
            }
            .toImmutableList()
    }

    private fun formatRate(rate: Double): String {
        val pattern = when {
            rate >= 100.0 -> "0.00"
            rate >= 1.0 -> "0.####"
            else -> "0.#####"
        }
        val symbols = DecimalFormatSymbols(Locale.US)
        return DecimalFormat(pattern, symbols).format(rate)
    }
}

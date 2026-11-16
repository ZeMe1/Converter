package kz.zeme.converter.home.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.converter.core.currency.domain.CurrencyFormatter
import kz.zeme.converter.core.currency.domain.CurrencyRates
import kz.zeme.converter.core.currency.domain.CurrencyRepository
import kz.zeme.converter.core.currency.view.presentation.navigation.CurrencyPickerNavigation
import kz.zeme.converter.core.domain.RequestCode
import kz.zeme.converter.core.locale.view.presentation.navigation.LanguagePickerNavigation
import kz.zeme.converter.core.navigation.NavigationResultLiveData
import kz.zeme.converter.core.navigation.NavigationResultLiveData.NavigationResult
import kz.zeme.converter.core.navigation.NavigationResultLiveData.NavigationResult.DialogResult
import kz.zeme.converter.core.navigation.Navigator
import kz.zeme.converter.core.observable.Event
import kz.zeme.converter.core.observable.EventFlow
import kz.zeme.converter.core.observable.MutableEventFlow
import kz.zeme.converter.core.observable.ViewState
import kz.zeme.converter.core.observable.extensions.getLoadingState
import kz.zeme.converter.core.observable.extensions.getViewState

private const val MAX_AMOUNT_LENGTH = 12
private const val DEFAULT_FROM_CODE = "USD"
private const val DEFAULT_TO_CODE = "KZT"

class HomeVM(
    private val currencyRepository: CurrencyRepository,
    private val currencyFormatter: CurrencyFormatter,
    private val navigationResult: NavigationResultLiveData,
) : ViewModel(), HomeController {

    private val _state = MutableLiveData<ViewState<HomeState>>()
    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)

    private var _loadRatesJob: Job? = null
    private var _rates: CurrencyRates? = null
    private var _fromCode = DEFAULT_FROM_CODE
    private var _toCode = DEFAULT_TO_CODE
    private var _rawAmount = ""

    val state: LiveData<ViewState<HomeState>> = _state
    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()

    init {
        _navigation.addSource(navigationResult, ::onNavigationResult)
    }

    override fun onStart() = loadRates()

    override fun onRefresh() = loadRates()

    override fun onAmountChange(amount: String) {
        val sanitized = amount.filter { it.isDigit() || it == '.' || it == ',' }
        if (sanitized == _rawAmount || sanitized.count { it == '.' || it == ',' } > 1) return
        _rawAmount = sanitized
        updateCalculations()
    }

    override fun onSwapCurrenciesClick() {
        _fromCode = _toCode.also { _toCode = _fromCode }
        loadRates()
    }

    override fun onOpenLanguagePickerClick() {
        _navigation.value = LanguagePickerNavigation.OpenLanguagePicker
    }

    override fun onPickFromCurrencyClick() = openCurrencyPicker(
        selectedCode = _fromCode,
        requestCode = RequestCode.RC_PICK_FROM_CURRENCY,
    )

    override fun onPickToCurrencyClick() = openCurrencyPicker(
        selectedCode = _toCode,
        requestCode = RequestCode.RC_PICK_TO_CURRENCY,
    )

    override fun onOtherRateCurrencyClick(code: String) {
        if (_toCode == code) return
        _toCode = code
        updateCalculations()
    }

    private fun openCurrencyPicker(selectedCode: String, requestCode: RequestCode) {
        _navigation.value = CurrencyPickerNavigation.OpenCurrencyPicker(
            baseCode = _fromCode,
            selectedCode = selectedCode,
            requestCode = requestCode,
        )
    }

    private fun onNavigationResult(event: Event<NavigationResult>) {
        val result = event.getContentIfNotHandled() as? DialogResult.Ok ?: return
        val currencyCode = result.data as? String ?: return

        when (result.requestCode) {
            RequestCode.RC_PICK_FROM_CURRENCY -> if (_fromCode != currencyCode) {
                _fromCode = currencyCode
                loadRates()
            }
            RequestCode.RC_PICK_TO_CURRENCY -> if (_toCode != currencyCode) {
                _toCode = currencyCode
                updateCalculations()
            }
        }
    }

    private fun loadRates() {
        _loadRatesJob?.cancel()
        _loadRatesJob = viewModelScope.launch {
            _state.value = _state.getLoadingState()

            val response = currencyRepository.getRates(_fromCode)

            _state.value = _state.getViewState(
                response = response,
                transform = { rates ->
                    _rates = rates
                    buildHomeState(rates)
                },
            )
        }
    }

    private fun updateCalculations() {
        val rates = _rates ?: return
        _state.value = ViewState.Data(buildHomeState(rates))
    }

    private fun buildHomeState(rates: CurrencyRates): HomeState {
        val cleanAmount = _rawAmount.replace(',', '.')
        val numericAmount = cleanAmount.toDoubleOrNull() ?: 0.0
        val isError = cleanAmount.length > MAX_AMOUNT_LENGTH

        val targetRateItem = rates.rates.find { it.code == _toCode }
        val targetRate = targetRateItem?.rate ?: 1.0

        val otherRateItems = rates.rates
            .filter { it.code != _fromCode }
            .map { rate ->
                HomeOtherRateItem(
                    code = rate.code,
                    name = rate.name,
                    formattedTotal = "${currencyFormatter.formatAmount(numericAmount * rate.rate)} ${rate.code}",
                    formattedPairRate = "1 $_fromCode = ${currencyFormatter.formatRate(rate.rate)} ${rate.code}",
                    isSelected = rate.code == _toCode,
                )
            }
            .toImmutableList()

        return HomeState(
            fromCode = _fromCode,
            fromName = rates.baseName,
            toCode = _toCode,
            toName = targetRateItem?.name.orEmpty(),
            rawAmount = _rawAmount,
            isAmountError = isError,
            formattedResultAmount = currencyFormatter.formatAmount(numericAmount * targetRate),
            formattedPairRate = "1 $_fromCode = ${currencyFormatter.formatRate(targetRate)} $_toCode",
            currenciesCount = otherRateItems.size,
            otherRates = otherRateItems,
        )
    }
}

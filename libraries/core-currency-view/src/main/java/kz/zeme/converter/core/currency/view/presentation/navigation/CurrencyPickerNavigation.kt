package kz.zeme.converter.core.currency.view.presentation.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.converter.core.domain.RequestCode
import kz.zeme.converter.core.navigation.Navigator

@Parcelize
sealed class CurrencyPickerNavigation : Navigator.Navigation {

    data class OpenCurrencyPicker(
        val baseCode: String,
        val selectedCode: String,
        val requestCode: RequestCode,
    ) : CurrencyPickerNavigation()
}

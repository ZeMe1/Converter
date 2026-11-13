package kz.zeme.converter.core.currency.view.presentation.navigation

import android.os.Bundle
import kz.zeme.converter.core.currency.view.presentation.CurrencyPickerFragment
import kz.zeme.converter.core.domain.RequestCode
import kz.zeme.converter.core.navigation.extensions.getTypedParcelable

class CurrencyPickerRouter {

    companion object {
        const val TAG_CURRENCY_PICKER = "TAG_CURRENCY_PICKER"

        private const val BASE_CODE_KEY = "BASE_CODE_KEY"
        private const val SELECTED_CODE_KEY = "SELECTED_CODE_KEY"
        private const val REQUEST_CODE_KEY = "REQUEST_CODE_KEY"
    }

    fun getCurrencyPickerFragment(
        baseCode: String,
        selectedCode: String,
        requestCode: RequestCode,
    ): CurrencyPickerFragment = CurrencyPickerFragment().apply {
        arguments = Bundle().apply {
            putString(BASE_CODE_KEY, baseCode)
            putString(SELECTED_CODE_KEY, selectedCode)
            putParcelable(REQUEST_CODE_KEY, requestCode)
        }
    }

    fun getBaseCodeOrNull(bundle: Bundle?): String? =
        bundle?.getString(BASE_CODE_KEY)

    fun getSelectedCodeOrNull(bundle: Bundle?): String? =
        bundle?.getString(SELECTED_CODE_KEY)

    fun getRequestCodeOrNull(bundle: Bundle?): RequestCode? =
        bundle?.getTypedParcelable(REQUEST_CODE_KEY)
}

package kz.zeme.converter.core.currency.view.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.converter.core.currency.view.presentation.navigation.CurrencyPickerRouter
import kz.zeme.converter.core.domain.RequestCode
import kz.zeme.converter.core.fragment.BaseBottomSheetDialog
import kz.zeme.converter.core.fragment.extensions.composeView
import kz.zeme.converter.core.navigation.BaseNavigation
import kz.zeme.converter.core.navigation.ExternalModulesNavigator
import kz.zeme.converter.core.navigation.Navigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CurrencyPickerFragment : BaseBottomSheetDialog() {

    private val currencyPickerRouter: CurrencyPickerRouter by inject()
    private val externalModulesNavigator: ExternalModulesNavigator by inject()

    private val baseCode: String
        get() = currencyPickerRouter.getBaseCodeOrNull(arguments)!!

    private val selectedCode: String
        get() = currencyPickerRouter.getSelectedCodeOrNull(arguments)!!

    private val requestCode: RequestCode
        get() = currencyPickerRouter.getRequestCodeOrNull(arguments)!!

    private val currencyPickerVM: CurrencyPickerVM by viewModel {
        parametersOf(baseCode, selectedCode, requestCode)
    }

    override val isExpanded: Boolean = true
    override val skipHalfCollapse: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        CurrencyPickerScreen(
            state = currencyPickerVM.state.observeAsState(),
            controller = currencyPickerVM,
        )
    }.also {
        currencyPickerVM.onStart()
    }

    override fun startObserving() {
        currencyPickerVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = ::navigate,
        )
    }

    private fun navigate(navigation: Navigator.Navigation) = when (navigation) {
        is BaseNavigation.GoBack,
        is BaseNavigation.Close -> dismiss()
        else -> externalModulesNavigator.navigate(navigation)
    }
}

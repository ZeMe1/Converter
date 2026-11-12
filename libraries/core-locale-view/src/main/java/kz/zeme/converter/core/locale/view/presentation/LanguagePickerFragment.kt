package kz.zeme.converter.core.locale.view.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.converter.core.fragment.BaseBottomSheetDialog
import kz.zeme.converter.core.fragment.extensions.composeView
import kz.zeme.converter.core.navigation.BaseNavigation
import kz.zeme.converter.core.navigation.ExternalModulesNavigator
import kz.zeme.converter.core.navigation.Navigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LanguagePickerFragment : BaseBottomSheetDialog() {

    private val languagePickerVM: LanguagePickerVM by viewModel()
    private val externalModulesNavigator: ExternalModulesNavigator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        LanguagePickerScreen(
            state = languagePickerVM.languages.observeAsState(),
            controller = languagePickerVM,
        )
    }.also {
        languagePickerVM.onStart()
    }

    override fun startObserving() {
        languagePickerVM.navigation.observeEvent(
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

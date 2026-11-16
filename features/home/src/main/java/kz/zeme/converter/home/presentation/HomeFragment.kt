package kz.zeme.converter.home.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.converter.core.fragment.BaseFragment
import kz.zeme.converter.core.fragment.extensions.composeView
import kz.zeme.converter.core.navigation.ExternalModulesNavigator
import kz.zeme.converter.core.navigation.Navigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private val externalModulesNavigator: ExternalModulesNavigator by inject()
    private val homeVM: HomeVM by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        HomeScreen(
            state = homeVM.state.observeAsState(),
            controller = homeVM,
        )
    }.also {
        homeVM.onStart()
    }

    override fun startObserving() {
        homeVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = ::navigate,
        )
    }

    private fun navigate(navigation: Navigator.Navigation) {
        externalModulesNavigator.navigate(navigation)
    }
}

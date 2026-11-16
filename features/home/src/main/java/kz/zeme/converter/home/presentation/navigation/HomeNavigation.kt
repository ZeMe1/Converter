package kz.zeme.converter.home.presentation.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.converter.core.navigation.Navigator

@Parcelize
sealed class HomeNavigation : Navigator.Navigation {

    data object OpenHome : HomeNavigation()
}

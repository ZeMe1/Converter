package kz.zeme.converter.core.locale.view.presentation.navigation

import kotlinx.parcelize.Parcelize
import kz.zeme.converter.core.navigation.Navigator

@Parcelize
sealed class LanguagePickerNavigation : Navigator.Navigation {

    data object OpenLanguagePicker : LanguagePickerNavigation()
}

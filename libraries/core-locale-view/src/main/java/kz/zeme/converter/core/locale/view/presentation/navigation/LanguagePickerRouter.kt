package kz.zeme.converter.core.locale.view.presentation.navigation

import kz.zeme.converter.core.fragment.BaseBottomSheetDialog
import kz.zeme.converter.core.locale.view.presentation.LanguagePickerFragment

class LanguagePickerRouter {

    companion object {
        const val TAG_LANGUAGE_PICKER = "TAG_LANGUAGE_PICKER"
    }

    fun getLanguagePickerFragment() = LanguagePickerFragment()
}

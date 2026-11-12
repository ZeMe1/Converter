package kz.zeme.converter.core.locale.view.presentation

import kz.zeme.converter.core.locale.domain.Language

interface LanguagePickerController {

    fun onLanguageClick(language: Language)
    fun onClose()
    fun onStart()
}

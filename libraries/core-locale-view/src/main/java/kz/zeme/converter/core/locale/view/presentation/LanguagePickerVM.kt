package kz.zeme.converter.core.locale.view.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.zeme.converter.core.domain.Response
import kz.zeme.converter.core.locale.domain.Language
import kz.zeme.converter.core.locale.domain.LanguageRepository
import kz.zeme.converter.core.navigation.BaseNavigation.Close
import kz.zeme.converter.core.navigation.BaseNavigation.ShowMain
import kz.zeme.converter.core.navigation.BaseNavigation.ShowToast
import kz.zeme.converter.core.navigation.Navigator
import kz.zeme.converter.core.observable.EventFlow
import kz.zeme.converter.core.observable.MutableEventFlow
import kz.zeme.converter.core.observable.ViewState
import kz.zeme.converter.core.observable.extensions.getErrorState
import kz.zeme.converter.core.observable.extensions.getLoadingState
import kz.zeme.converter.core.observable.extensions.getViewState
import kz.zeme.converter.core.res.R

open class LanguagePickerVM(
    private val languageRepository: LanguageRepository,
) : ViewModel(), LanguagePickerController {

    private val _navigation = MutableEventFlow<Navigator.Navigation>(viewModelScope)
    private val _languages = MutableLiveData<ViewState<List<Language>>>()

    private var getLanguagesJob: Job? = null
    private var saveLanguageJob: Job? = null

    val navigation: EventFlow<Navigator.Navigation> = _navigation.asEventFlow()
    val languages: LiveData<ViewState<List<Language>>> = _languages

    override fun onLanguageClick(language: Language) = onSaveSelectedLanguage(language)

    override fun onClose() {
        _navigation.value = Close
    }

    override fun onStart() = onLoadLanguages()

    private fun onLoadLanguages() {
        if (getLanguagesJob?.isActive == true) return

        getLanguagesJob = loadLanguages()
    }

    private fun loadLanguages() = viewModelScope.launch {
        _languages.value = _languages.getLoadingState()

        val response = languageRepository.getLanguages()

        _languages.value = _languages.getViewState(response)
    }

    private fun onSaveSelectedLanguage(language: Language) {
        if (saveLanguageJob?.isActive == true) return

        saveLanguageJob = saveLanguage(language)
    }

    private fun saveLanguage(language: Language) = viewModelScope.launch {
        _languages.value = _languages.getLoadingState()

        when (val response = languageRepository.saveSelectedLanguage(language)) {
            is Response.Failure -> _languages.value = _languages.getErrorState(response)
            is Response.Success -> {
                _navigation.value = ShowToast.MessageRes(R.string.language_picker_language_changed_message)
                _navigation.value = ShowMain(
                    restart = true,
                    withSplash = true,
                )
            }
        }
    }
}

package com.hamaro.rockettranslatenativeapp.ui.presentation.camera

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hamaro.rockettranslatenativeapp.data.remote.model.Language
import com.hamaro.rockettranslatenativeapp.domain.TextTranslationService
import com.hamaro.rockettranslatenativeapp.domain.base.BaseViewModel
import com.hamaro.rockettranslatenativeapp.domain.model.LanguageType
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import com.hamaro.rockettranslatenativeapp.domain.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TranslationViewModel(
    private val translationService: TextTranslationService
) : BaseViewModel()
{
    var translationState = mutableStateOf<TranslationUiState>(TranslationUiState.Idle)
        private set

    var currentText = mutableStateOf("")
     private set

    private var _sourceLanguages : MutableStateFlow<UiState<List<Language>>> = MutableStateFlow(UiState.Idle)
    val sourceLanguages : StateFlow<UiState<List<Language>>> = _sourceLanguages.asStateFlow()

    private var _targetLanguages : MutableStateFlow<UiState<List<Language>>> = MutableStateFlow(UiState.Idle)
    val targetLanguages : StateFlow<UiState<List<Language>>> = _sourceLanguages.asStateFlow()

    init {
        getSourceLanguages()
        getTargetLanguages()
    }

    fun translateText(sourceText: String, sourceLanguage: String? = null, targetLanguage: String) {
        translationState.value = TranslationUiState.Loading

        viewModelScope.launch {
            val result = translationService.translate(sourceText, sourceLanguage, targetLanguage)

            translationState.value = when (result) {
                is RequestState.Success -> TranslationUiState.Success(result.data.text)
                is RequestState.Error -> TranslationUiState.Error(result.message)
                RequestState.Idle -> TranslationUiState.Idle
                RequestState.Loading -> TranslationUiState.Loading
            }
        }
    }

    fun getSourceLanguages() {
        viewModelScope.launch {
            try {
                translationService.getLanguages(
                    LanguageType.SOURCE
                ).collect { languageList ->
                    when (languageList) {
                        is RequestState.Error -> {
                           _sourceLanguages.value = UiState.Error(languageList.message)
                        }
                        RequestState.Idle -> Unit
                        RequestState.Loading -> {
                            _sourceLanguages.value =  UiState.Loading
                        }
                        is RequestState.Success -> {
                            _sourceLanguages.value = UiState.Success(languageList.getSuccessData())
                        }
                    }
                }
            } catch (
                e : Exception
            ) {
                _sourceLanguages.value = UiState.Error(e.message ?: "Sorry, unexpected error occurred, please try again")
            }
        }
    }

    fun getTargetLanguages() {
        viewModelScope.launch {
            try {
                translationService.getLanguages(
                    LanguageType.TARGET
                ).collect { languageList ->
                    when (languageList) {
                        is RequestState.Error -> {
                            _targetLanguages.value = UiState.Error(languageList.message)
                        }
                        RequestState.Idle -> Unit
                        RequestState.Loading -> {
                            _targetLanguages.value =  UiState.Loading
                        }
                        is RequestState.Success -> {
                            _targetLanguages.value = UiState.Success(languageList.getSuccessData())
                        }
                    }
                }
            } catch (
                e : Exception
            ) {
                _targetLanguages.value = UiState.Error(e.message ?: "Sorry, unexpected error occurred, please try again")
            }
        }
    }

    fun updateCurrentText(text : String) {
        currentText.value = text
    }
}

sealed class TranslationUiState{
    data object Idle : TranslationUiState()
    data object Loading : TranslationUiState()
    data class Success(val text : String) : TranslationUiState()
    data class Error(val message : String) : TranslationUiState()
}
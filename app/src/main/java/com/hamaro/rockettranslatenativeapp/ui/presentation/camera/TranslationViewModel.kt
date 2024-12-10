package com.hamaro.rockettranslatenativeapp.ui.presentation.camera

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hamaro.rockettranslatenativeapp.domain.TextTranslationService
import com.hamaro.rockettranslatenativeapp.domain.base.BaseViewModel
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import kotlinx.coroutines.launch

class TranslationViewModel(
    private val translationService: TextTranslationService
) : BaseViewModel()
{
    var translationState = mutableStateOf<TranslationUiState>(TranslationUiState.Idle)
        private set

    var currentText = mutableStateOf("")
     private set

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
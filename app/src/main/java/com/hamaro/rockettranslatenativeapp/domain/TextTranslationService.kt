package com.hamaro.rockettranslatenativeapp.domain

import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import com.hamaro.rockettranslatenativeapp.domain.model.TranslatedText

interface TextTranslationService {
    fun translate(sourceText : String, sourceLanguage : String? = null,  targetLanguage : String) : RequestState<TranslatedText>
}
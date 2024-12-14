package com.hamaro.rockettranslatenativeapp.domain

import com.hamaro.rockettranslatenativeapp.data.remote.model.Language
import com.hamaro.rockettranslatenativeapp.domain.model.LanguageType
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import com.hamaro.rockettranslatenativeapp.domain.model.TranslatedText
import kotlinx.coroutines.flow.Flow

interface TextTranslationService {
    suspend fun translate(sourceText : String, sourceLanguage : String? = null,  targetLanguage : String) : RequestState<TranslatedText>
    fun getLanguages(type : LanguageType) : Flow<RequestState<List<Language>>>
}
package com.hamaro.rockettranslatenativeapp.data.remote.repository

import com.hamaro.rockettranslatenativeapp.data.remote.model.Language
import com.hamaro.rockettranslatenativeapp.data.remote.model.TranslationRequest
import com.hamaro.rockettranslatenativeapp.domain.TextTranslationService
import com.hamaro.rockettranslatenativeapp.domain.TranslationApiRepository
import com.hamaro.rockettranslatenativeapp.domain.model.LanguageType
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import com.hamaro.rockettranslatenativeapp.domain.model.TranslatedText
import com.hamaro.rockettranslatenativeapp.domain.model.toTranslatedText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TextTranslationRepositoryImpl(
    private val translationApi :  TranslationApiRepository
) : TextTranslationService {

    override suspend fun translate(
        sourceText: String,
        sourceLanguage: String?,
        targetLanguage: String
    ): RequestState<TranslatedText> {
        return try {
            val requestBlock = TranslationRequest(
                text = listOf(sourceText),
                source_lang = sourceLanguage,
                target_lang = targetLanguage
            )
            val translateTextResponse = translationApi.translateText(requestBlock)

            val toTranslatedText = translateTextResponse.toTranslatedText()

            if (toTranslatedText.text.isNotBlank()) {
                RequestState.Success(toTranslatedText)
            } else {
                RequestState.Error("Translation text is empty, try again")
            }

        } catch (e: Exception){
            println("Error while translating text: ${e.message}")
            RequestState.Error(e.message ?: "Error while translating text")
        }
    }

    override fun getLanguages(type: LanguageType): Flow<RequestState<List<Language>>> {
        return flow {
           try {
               emit(RequestState.Loading)
               val languages = translationApi.getLanguages(type.type)

               if (!languages.isNullOrEmpty()) {
                   emit(RequestState.Success(languages))
               } else {
                   emit(RequestState.Error("The fetched language list is empty"))
               }

           } catch (e : Exception) {
               emit(RequestState.Error("Error while fetching languages, please check your network connection"))
           }
        }
    }
}
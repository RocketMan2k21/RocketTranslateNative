package com.hamaro.rockettranslatenativeapp.data.remote.repository

import com.deepl.api.Translator
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import com.hamaro.rockettranslatenativeapp.domain.TextTranslationService
import com.hamaro.rockettranslatenativeapp.domain.model.TranslatedText
import org.koin.android.BuildConfig

class TextTranslationServiceImpl(
) : TextTranslationService {

    private lateinit var translator: Translator

    override fun translate(sourceText: String, sourceLanguage: String?, targetLanguage: String): RequestState<TranslatedText> {
        translator = Translator(BuildConfig.TRANSLATION_API_KEY)

        return try {
            val translatedText = translator.translateText(sourceText, sourceLanguage, targetLanguage)

            if (translatedText != null) {
                println("Translated Text: $translatedText")
                val text = translatedText.text
                val detectedSourceLanguage = translatedText.detectedSourceLanguage ?: ""
                if (text != null) {
                    RequestState.Success(TranslatedText(text, detectedSourceLanguage))
                } else {
                    RequestState.Error("Translated text is null")
                }
            } else {
                RequestState.Error("Error while translating text, please try again")
            }
        } catch (e: Exception) {
            println("Translation error: ${e.message}")
            RequestState.Error(e.message ?: "An unknown error occurred")
        }
    }

    enum class TargetLanguage(val language : String) {
        UA("uk"),
        DE("de"),
        EN("en")
    }
}
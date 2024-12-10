package com.hamaro.rockettranslatenativeapp.domain.model

import com.hamaro.rockettranslatenativeapp.data.remote.model.TranslationResponse

fun TranslationResponse.toTranslatedText() : TranslatedText {
    val firstTranslation = translations[0]
    return TranslatedText(
        text = firstTranslation.text,
        detectedSourceLanguage = firstTranslation.detectedSourceLang
    )
}
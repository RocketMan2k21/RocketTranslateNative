package com.hamaro.rockettranslatenativeapp.data.remote.model

data class TranslationRequest (
    val text : List<String>,
    val target_lang : String,
    val source_lang : String?
)
package com.hamaro.rockettranslatenativeapp.data.remote.model

import com.google.gson.annotations.SerializedName

data class TranslationResponse (
    @SerializedName("translations")
    val translations : List<Translation>
)

data class Translation(
    @SerializedName("detected_source_language")
    val detectedSourceLang : String,

    @SerializedName("text")
    val text : String
)

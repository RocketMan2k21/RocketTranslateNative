package com.hamaro.rockettranslatenativeapp.data.remote.model

import com.google.gson.annotations.SerializedName


data class Language(
    @SerializedName("language")
    val language : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("supports_formality")
    val supportsFormality : Boolean
)
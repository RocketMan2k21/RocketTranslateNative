package com.hamaro.rockettranslatenativeapp.domain.model

enum class TargetLanguage(val lang : String) {
    DE("DE"),
    EN("EN"),
    FR("FR")
}

val languageHashMap = hashMapOf(
    TargetLanguage.EN to "English",
    TargetLanguage.DE to "German",
    TargetLanguage.FR to "French"
)
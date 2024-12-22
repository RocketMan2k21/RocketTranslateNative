package com.hamaro.rockettranslatenativeapp.domain.model

enum class TargetLanguage(val lang : String) {
    DE("DE"),
    EN("EN"),
    FR("FR")
}

enum class LanguageType(val type : String) {
    SOURCE("source"),
    TARGET("target")
}

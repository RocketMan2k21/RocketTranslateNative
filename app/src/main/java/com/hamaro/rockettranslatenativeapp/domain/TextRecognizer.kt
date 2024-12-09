package com.hamaro.rockettranslatenativeapp.domain

import com.hamaro.rockettranslatenativeapp.domain.model.RequestState

interface TextRecognizer {
    suspend fun recognizeText(base64encoded: String) : RequestState<String>
}

package com.hamaro.rockettranslatenativeapp.domain

import android.graphics.Bitmap
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState

interface TextRecognizer {
    suspend fun recognizeText(bitmap: Bitmap) : RequestState<String>
}

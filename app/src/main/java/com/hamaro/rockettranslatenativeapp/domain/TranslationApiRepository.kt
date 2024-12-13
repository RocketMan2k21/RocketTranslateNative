package com.hamaro.rockettranslatenativeapp.domain

import com.hamaro.rockettranslatenativeapp.data.remote.model.TranslationRequest
import com.hamaro.rockettranslatenativeapp.data.remote.model.TranslationResponse
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import retrofit2.http.Body
import retrofit2.http.POST

interface TranslationApiRepository {
    @POST("v2/translate")
    suspend fun translateText(@Body body : TranslationRequest) : TranslationResponse
}
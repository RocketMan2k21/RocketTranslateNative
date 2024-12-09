package com.hamaro.rockettranslatenativeapp.domain

import com.hamaro.rockettranslatenativeapp.domain.model.ImageRequest
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState

interface ImageRepository {
    suspend fun saveImageRequest(imageRequest: ImageRequest) : RequestState<String>
    suspend fun getAllImages() : RequestState<List<ImageRequest>>
}
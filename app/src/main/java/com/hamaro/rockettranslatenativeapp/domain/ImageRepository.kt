package com.hamaro.rockettranslatenativeapp.domain

import com.hamaro.rockettranslatenativeapp.data.remote.model.ImageRequest
import com.hamaro.rockettranslatenativeapp.domain.model.ImageHistory
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState

interface ImageRepository {
    suspend fun saveImageRequest(imageRequest: ImageRequest) : RequestState<String>
    suspend fun getAllImages() : RequestState<ImageHistory>
}
package com.hamaro.rockettranslatenativeapp.domain

import com.hamaro.rockettranslatenativeapp.data.remote.model.ImageRequest
import com.hamaro.rockettranslatenativeapp.domain.model.ImageHistory
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import kotlinx.coroutines.flow.Flow

interface ImageRepository {
    suspend fun saveImageRequest(imageRequest: ImageRequest) : RequestState<String>
    suspend fun getAllImages() : Flow<RequestState<ImageHistory>>
}
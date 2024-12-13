package com.hamaro.rockettranslatenativeapp.ui.presentation.history;

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hamaro.rockettranslatenativeapp.data.remote.model.ImageRequest
import com.hamaro.rockettranslatenativeapp.domain.ImageRepository
import com.hamaro.rockettranslatenativeapp.domain.model.ImageFirestore
import com.hamaro.rockettranslatenativeapp.domain.model.RequestState
import com.hamaro.rockettranslatenativeapp.domain.model.UiState
import com.roman_duda.rockettranslateapp.utils.ImageUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ImageViewModel(
    private val imageRepository: ImageRepository
) : ViewModel(){

    private val _images: MutableStateFlow<UiState<List<ImageFirestore>>> = MutableStateFlow(UiState.Idle)
    var images : StateFlow<UiState<List<ImageFirestore>>> = _images.asStateFlow()

    var savedImageState : MutableState<UiState<String>> = mutableStateOf(UiState.Idle)
        private set

    init {
        fetchImages()
    }

    private fun fetchImages() {
        try {
            viewModelScope.launch {
                imageRepository.getAllImages()
                    .collect { images ->
                        when(images) {
                            is RequestState.Error -> {
                                _images.value = UiState.Error(images.message)
                            }
                            RequestState.Idle -> {}
                            RequestState.Loading -> {
                                _images.value = UiState.Loading
                            }
                            is RequestState.Success -> {
                                val allImages = images.data.images
                                if (allImages.isNotEmpty())
                                    _images.value = UiState.Success(allImages)
                                else
                                    _images.value = UiState.Error("You don't have image history yet")
                            }
                        }
                    }
            }
        } catch (e: Exception) {
            _images.value = UiState.Error(e.message ?: "Error while fetching image history, please try again later")
        }
    }

    fun saveImage(context : Context, imageUri : Uri) {
        try {
            savedImageState.value = UiState.Loading
            val base64 = ImageUtils.uriToBase64(context, imageUri)

            val imageRequest =
            base64?.let {
                ImageRequest(
                    base64 = it,
                    dateTimeCaptured = getCurrentDateFormatted()
                )
            }

            imageRequest?.let {
                viewModelScope.launch {
                    val result = imageRepository.saveImageRequest(imageRequest)

                    if (result.isSuccess()) {
                        savedImageState.value = UiState.Success(result.getSuccessData())
                    } else {
                        savedImageState.value = UiState.Error(result.getErrorMessage())
                    }
                }
            }

        }catch (e: Exception) {
            savedImageState.value = UiState.Error(e.message ?: "Unknown error while saving image, please try again")
        }
    }



    fun getCurrentDateFormatted(): String {
        // Get current date
        val currentDate = LocalDate.now()

        // Define the desired format
        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

        // Format the current date
        return currentDate.format(formatter)
    }


}

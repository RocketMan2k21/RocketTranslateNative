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
import com.hamaro.rockettranslatenativeapp.domain.base.BaseViewModel
import com.hamaro.rockettranslatenativeapp.domain.model.ImageFirestore
import com.hamaro.rockettranslatenativeapp.domain.model.UiState
import com.roman_duda.rockettranslateapp.utils.ImageUtils
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryImageViewModel(
    private val imageRepository: ImageRepository
) : ViewModel(){

    private val _images: MutableState<UiState<List<ImageFirestore>>> = mutableStateOf(UiState.Idle)
    var images : State<UiState<List<ImageFirestore>>> = _images

    var savedImageState : MutableState<UiState<String>> = mutableStateOf(UiState.Idle)
        private set

    init {
        fetchImages()
    }

    private fun fetchImages() {
        try {
            viewModelScope.launch {
                _images.value = UiState.Loading
                val result = imageRepository.getAllImages()

                if (result.isSuccess()) {
                    val allImages = result.getSuccessData().images
                    if (allImages.isNotEmpty())
                        _images.value = UiState.Success(allImages)
                    else
                        _images.value = UiState.Error("You don't have image history yet")
                } else {
                    _images.value = UiState.Error("Error while fetching image history, please try again later")
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

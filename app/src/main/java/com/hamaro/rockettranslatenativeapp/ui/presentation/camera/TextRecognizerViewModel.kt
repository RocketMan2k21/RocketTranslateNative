package com.hamaro.rockettranslatenativeapp.ui.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.hamaro.rockettranslatenativeapp.domain.TextRecognizer
import com.hamaro.rockettranslatenativeapp.domain.base.BaseViewModel
import com.roman_duda.rockettranslateapp.utils.ImageUtils
import kotlinx.coroutines.launch

class TextRecognizerViewModel(
    val textRecognizer: TextRecognizer,
) : BaseViewModel() {

    private val _imageText: MutableState<ImageTextUiState> = mutableStateOf(ImageTextUiState.Idle)
    val imageText: State<ImageTextUiState> = _imageText

    fun recognizeImage(bitMap: Bitmap) {
        _imageText.value = ImageTextUiState.Loading

        val scaledBitMap = ImageUtils.scaleBitmapDown(
            bitMap,
            maxDimension = 640
        )

        try {
            viewModelScope.launch {
                val result = textRecognizer
                    .recognizeText(scaledBitMap)

                if (!result.isError())
                    if (result.getSuccessData().isNotBlank())
                        _imageText.value = ImageTextUiState.Success(result.getSuccessData())
                    else {
                        _imageText.value = ImageTextUiState.Error("No text Recognized, please try again")
                    }
                else {
                    _imageText.value = ImageTextUiState.Error("Unable to process image, please try again")
                }
            }
        } catch (e: Exception) {
            _imageText.value = ImageTextUiState.Error("Unable to process image, please try again")
        }
    }
}

sealed class ImageTextUiState {
    data object Idle : ImageTextUiState()
    data object Loading : ImageTextUiState()
    data class Success(val text : String) : ImageTextUiState()
    data class Error(val message : String) : ImageTextUiState()
}
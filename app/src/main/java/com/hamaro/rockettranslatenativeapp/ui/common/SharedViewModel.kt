package com.hamaro.rockettranslatenativeapp.ui.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.hamaro.rockettranslatenativeapp.domain.model.ImageFirestore

class SharedViewModel : ViewModel() {
    private val _base64Image : MutableState<String> = mutableStateOf("")
    val base64Image: State<String> get() = _base64Image

    fun setBase64Image(base64: String) {
        _base64Image.value = base64
    }
}
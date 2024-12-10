package com.hamaro.rockettranslatenativeapp.ui.presentation.camera

import android.content.Context
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hamaro.rockettranslatenativeapp.data.CameraCaptureService
import com.hamaro.rockettranslatenativeapp.domain.model.TargetLanguage
import com.hamaro.rockettranslatenativeapp.ui.theme.onPrimaryTextColor
import org.koin.androidx.compose.koinViewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraPreview(
   viewModel: TextRecognizerViewModel = koinViewModel(),
   translationViewModel : TranslationViewModel = koinViewModel()
) {

    val imageText by viewModel.imageText
    val translatedText by translationViewModel.translationState

    val currentText by translationViewModel.currentText

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember {
        ImageCapture.Builder().build()
    }

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
        preview.surfaceProvider = previewView.surfaceProvider
    }
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier
        .fillMaxSize()
        ) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            Button(
                modifier = Modifier
                    .padding(bottom =
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                onClick = {
                    CameraCaptureService.captureImage(
                        imageCapture,
                        context,
                        onSuccess = { uri ->
                            viewModel.recognizeImage(context, uri)
                        },
                        onError = {
                            Toast.makeText(
                                context,
                                "Error while capturing image, please try again",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }) {
                Text(text = "Capture Image")
            }

            Button(
                modifier = Modifier
                    .padding(bottom =
                    WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                onClick = {
                    translationViewModel.translateText(currentText, targetLanguage = TargetLanguage.DE.lang)
                },
                enabled = currentText.isNotBlank()
            ) {
                Text(text = "Translate text")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

            when (imageText) {
                is ImageTextUiState.Error -> {
                    val error = (imageText as ImageTextUiState.Error).message
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }

                ImageTextUiState.Idle -> {

                }

                ImageTextUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is ImageTextUiState.Success -> {
                    val data = (imageText as ImageTextUiState.Success).text
                    translationViewModel.updateCurrentText(data)
                    AnimatedContent(data, label = "") { text: String ->
                        Text(text, color = onPrimaryTextColor)
                    }
                }
            }

            when (translatedText) {
                is TranslationUiState.Error -> {
                    val error = (translatedText as TranslationUiState.Error).message
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }

                TranslationUiState.Idle -> {

                }

                TranslationUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is TranslationUiState.Success -> {
                    val data = (translatedText as TranslationUiState.Success).text
                    AnimatedContent(data, label = "") { text: String ->
                        Text(text, color = onPrimaryTextColor)
                    }
                }

        }
    }


}

private suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this).also { cameraProvider ->
            cameraProvider.addListener({
                continuation.resume(cameraProvider.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }
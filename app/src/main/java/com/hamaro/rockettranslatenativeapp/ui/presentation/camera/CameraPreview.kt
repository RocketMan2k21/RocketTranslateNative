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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hamaro.rockettranslatenativeapp.data.CameraCaptureService
import org.koin.androidx.compose.koinViewModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraPreview(
   viewModel: TextRecognizerViewModel = koinViewModel()
) {

    val imageText by viewModel.imageText

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
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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
                AnimatedContent(data, label = "") { text: String ->
                    Text(text)
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
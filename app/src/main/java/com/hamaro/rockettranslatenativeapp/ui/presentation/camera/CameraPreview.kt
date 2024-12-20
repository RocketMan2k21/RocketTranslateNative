package com.hamaro.rockettranslatenativeapp.ui.presentation.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.hamaro.rockettranslatenativeapp.R
import com.hamaro.rockettranslatenativeapp.data.CameraCaptureService
import com.hamaro.rockettranslatenativeapp.ui.common.FeedbackIconButton
import com.hamaro.rockettranslatenativeapp.ui.presentation.history.ImageViewModel
import com.hamaro.rockettranslatenativeapp.ui.theme.Typography
import com.hamaro.rockettranslatenativeapp.ui.theme.backGroundForCameraPreviewBounds
import com.hamaro.rockettranslatenativeapp.ui.theme.cameraPreviewIconColor
import com.hamaro.rockettranslatenativeapp.ui.theme.onPrimaryTextColor
import com.hamaro.rockettranslatenativeapp.ui.theme.textCameraColor
import com.roman_duda.rockettranslateapp.utils.ImageUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.io.File

@Composable
fun CameraPreview(
    viewModel: TextRecognizerViewModel = koinViewModel(),
    translationViewModel : TranslationViewModel = koinViewModel(),
    imageViewModel : ImageViewModel = koinViewModel(),
    navigateToPhotoHistory : () -> Unit,
    navigateToPhoto : (String) -> Unit
) {

    val imageText by remember {viewModel.imageText}

    val coroutineScope = rememberCoroutineScope()

    val lensFacing = CameraSelector.LENS_FACING_BACK
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context)
    }
    val cameraxSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    val imageCapture = remember {
        ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri = data?.data
            selectedImageUri?.let {
                val uriToBase64 = ImageUtils.uriToBase64(context, it)
                uriToBase64?.let { base64 ->
                    navigateToPhoto(base64)
                }
            }
        }
    }

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner, cameraxSelector, preview, imageCapture
            )
            preview.surfaceProvider = previewView.surfaceProvider
        } catch (e: Exception) {
            Log.e("CameraPreview", "Failed to bind camera use cases: ${e.message}")
        }
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier
        .fillMaxSize()
        ) {
        AndroidView({ previewView }, modifier = Modifier.fillMaxSize())

        UpperOptionBlock(modifier = Modifier.align(Alignment.TopCenter), navigateToPhotoHistory = navigateToPhotoHistory)


        Box(){

        }
        IconButton(
            modifier = Modifier
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                .padding(16.dp)
                .align(Alignment.BottomStart),
            onClick = {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                galleryLauncher.launch(intent)
            }
        ) {
            Icon(
                modifier = Modifier.size(40.dp),
                painter = painterResource(R.drawable.baseline_photo_library_24),
                tint = cameraPreviewIconColor,
                contentDescription = "galleryPick"
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
            horizontalArrangement = Arrangement.SpaceAround // This centers the content of the Row
        ) {
            // Spacer to push IconButton to the left

            // MainButtonCapture in the center
            MainButtonCapture(
                modifier = Modifier
            ) {
                CameraCaptureService.captureImage(
                    imageCapture,
                    context,
                    onSuccess = { uri ->
                        imageViewModel.saveImage(context, uri)
                        val uriToBase64 = ImageUtils.uriToBase64(context, uri)
                        uriToBase64?.let {
                            navigateToPhoto(it)
                        }
                        uri.path?.let { File(it).delete() }
                    },
                    onError = {

                        Toast.makeText(
                            context,
                            "Error while capturing image, please try again",
                            Toast.LENGTH_LONG
                        ).show()
                        coroutineScope.launch { restartCamera(context, lifecycleOwner, cameraxSelector, preview, imageCapture) }
                    }
                )
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
    }


}

private suspend fun restartCamera(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraxSelector: CameraSelector,
    preview: Preview,
    imageCapture: ImageCapture
) {
    val cameraProvider = context.getCameraProvider()
    cameraProvider.unbindAll()
    cameraProvider.bindToLifecycle(lifecycleOwner, cameraxSelector, preview, imageCapture)
}

@Composable
private fun UpperOptionBlock(
    modifier: Modifier = Modifier,
    navigateToPhotoHistory: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = backGroundForCameraPreviewBounds)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    ,
                painter = painterResource(R.drawable.baseline_flash_on_24),
                tint = Color.White,
                contentDescription = ""
            )

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clickable { navigateToPhotoHistory() },
                painter = painterResource(R.drawable.baseline_history_24),
                tint = Color.White,
                contentDescription = ""
            )

            FeedbackIconButton(
                modifier = Modifier

            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun PreviewUpperBlock() {
    UpperOptionBlock {

    }
}

@Composable
fun MainButtonCapture(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .size(80.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        border = BorderStroke(2.dp, color = cameraPreviewIconColor),
        shape = CircleShape,
        color = Color.Transparent
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(5.dp)
                .clip(CircleShape)
                .background(color = cameraPreviewIconColor)
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun PreviewMainButton(){
    MainButtonCapture {  }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    withContext(Dispatchers.IO) {
        ProcessCameraProvider.getInstance(this@getCameraProvider).get()
    }
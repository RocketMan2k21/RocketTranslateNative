package com.hamaro.rockettranslatenativeapp.ui.presentation.image

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.hamaro.rockettranslatenativeapp.domain.model.TargetLanguage
import com.hamaro.rockettranslatenativeapp.ui.common.CustomTopBarWithReturn
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.ImageTextUiState
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TextRecognizerViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TranslationUiState
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TranslationViewModel
import com.hamaro.rockettranslatenativeapp.ui.theme.onPrimaryTextColor
import com.roman_duda.rockettranslateapp.utils.ImageUtils
import com.roman_duda.rockettranslateapp.utils.decodeBase64ToByteArray
import com.roman_duda.rockettranslateapp.utils.toImageBitmap
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImageScreen(
    imageBase64 : String,
    translationViewModel: TranslationViewModel = koinViewModel(),
    textRecognizerViewModel: TextRecognizerViewModel = koinViewModel(),
    navigateHome: () -> Unit
) {
    val context = LocalContext.current

    val imageTextState by remember { textRecognizerViewModel.imageText }
    val translationState by remember { translationViewModel.translationState }
    val currentText by remember { translationViewModel.currentText }

    val isTextRecognized by remember { derivedStateOf { currentText.isNotBlank() } }
    Log.d("Debug", "current image: ${imageBase64.isNotBlank()}")

    LaunchedEffect(Unit) {
        textRecognizerViewModel.recognizeImage(imageBase64.decodeBase64ToByteArray().toImageBitmap().asAndroidBitmap())
    }

    Scaffold(
        topBar = {
            CustomTopBarWithReturn(
                modifier = Modifier.padding(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
                title = "",
                onClickBack = navigateHome
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Log.d("Debug", "imageBase64 is not empty : ${imageBase64.isNotBlank()}")
            Image(
                bitmap = imageBase64.decodeBase64ToByteArray().toImageBitmap(),
                contentDescription = "My image",
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (imageTextState) {
                is ImageTextUiState.Error -> {
                    val error = (imageTextState as ImageTextUiState.Error).message
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
                ImageTextUiState.Idle -> Unit
                ImageTextUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is ImageTextUiState.Success -> {
                    val recognizedText = (imageTextState as ImageTextUiState.Success).text
                    translationViewModel.updateCurrentText(recognizedText)
                    AnimatedContent(targetState = recognizedText, label = "") { text ->
                        Text(text = text, color = onPrimaryTextColor)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Handle Translation State
            when (translationState) {
                is TranslationUiState.Error -> {
                    val error = (translationState as TranslationUiState.Error).message
                    Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                }
                TranslationUiState.Idle -> Unit
                TranslationUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is TranslationUiState.Success -> {
                    val translatedText = (translationState as TranslationUiState.Success).text
                    AnimatedContent(targetState = translatedText, label = "") { text ->
                        Text(text = text, color = onPrimaryTextColor)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Translate Button
            if (isTextRecognized) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 16.dp,
                            vertical = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                        ),
                    onClick = {
                        translationViewModel.translateText(
                            currentText,
                            targetLanguage = TargetLanguage.DE.lang
                        )
                    }
                ) {
                    Text(text = "Translate Text")
                }
            }
        }
    }
}

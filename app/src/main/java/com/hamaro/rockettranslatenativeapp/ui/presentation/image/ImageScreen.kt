package com.hamaro.rockettranslatenativeapp.ui.presentation.image

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Surface
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.material.chip.Chip
import com.hamaro.rockettranslatenativeapp.data.remote.model.Language
import com.hamaro.rockettranslatenativeapp.domain.model.TargetLanguage
import com.hamaro.rockettranslatenativeapp.domain.model.UiState
import com.hamaro.rockettranslatenativeapp.ui.common.CustomTopBarWithReturn
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.ImageTextUiState
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TextRecognizerViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TranslationUiState
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TranslationViewModel
import com.hamaro.rockettranslatenativeapp.ui.theme.Typography
import com.hamaro.rockettranslatenativeapp.ui.theme.backgroundColor
import com.hamaro.rockettranslatenativeapp.ui.theme.borderStrokeColor
import com.hamaro.rockettranslatenativeapp.ui.theme.grayBlack
import com.hamaro.rockettranslatenativeapp.ui.theme.onPrimaryTextColor
import com.roman_duda.rockettranslateapp.utils.ImageUtils
import com.roman_duda.rockettranslateapp.utils.decodeBase64ToByteArray
import com.roman_duda.rockettranslateapp.utils.toImageBitmap
import org.koin.androidx.compose.koinViewModel

@Composable
fun ImageScreen(
    imageBase64: String,
    translationViewModel: TranslationViewModel = koinViewModel(),
    textRecognizerViewModel: TextRecognizerViewModel = koinViewModel(),
    navigateHome: () -> Unit
) {
    val context = LocalContext.current

    val imageTextState by remember { textRecognizerViewModel.imageText }
    val translationState by remember { translationViewModel.translationState }
    val currentText by remember { translationViewModel.currentText }

    val availableSourceLanguages by translationViewModel.sourceLanguages.collectAsState()
    val availableTargetLanguages by translationViewModel.targetLanguages.collectAsState()

    val isTextRecognized by remember { derivedStateOf { currentText.isNotBlank() } }
    Log.d("Debug", "current image: ${imageBase64.isNotBlank()}")

    LaunchedEffect(Unit) {
        textRecognizerViewModel.recognizeImage(
            imageBase64.decodeBase64ToByteArray().toImageBitmap().asAndroidBitmap()
        )
    }

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        val bitmap = imageBase64.decodeBase64ToByteArray().toImageBitmap()
        val rotatedBitmap = ImageUtils.rotateBitmapIfNeeded(bitmap.asAndroidBitmap())
        val imageBitmap = rotatedBitmap.asImageBitmap()

        Log.d("Debug", "imageBase64 is not empty : ${imageBase64.isNotBlank()}")

        var selectedSourceLanguageText by remember { mutableStateOf("Detected language") }
        var selectedTargetLanguageText by remember { mutableStateOf("...") }

        var selectedSourceLanguage: String? by remember { mutableStateOf(null) }
        var selectedTargetLanguage: String? by remember { mutableStateOf(null) }

        var translatedText by remember {mutableStateOf("")}

        Box(
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(),
                bitmap = imageBitmap,
                contentDescription = "My image",
                contentScale = ContentScale.Crop
            )


            androidx.compose.animation.AnimatedVisibility(isTextRecognized) {
                TranslateOptions(
                    modifier = Modifier
                        .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 16.dp),
                    availableSourceLanguages = (availableSourceLanguages as? UiState.Success)?.data
                        ?: emptyList(),
                    availableTargetLanguages = (availableTargetLanguages as? UiState.Success)?.data
                        ?: emptyList(),
                    onSelectedTargetLanguage = { language ->
                        selectedTargetLanguage = language.language
                        selectedTargetLanguageText = language.name
                        selectedTargetLanguage?.let {
                            translationViewModel.translateText(
                                sourceText = currentText,
                                sourceLanguage = selectedSourceLanguage,
                                targetLanguage = language.language,
                            )
                        }
                    },
                    onSelectedSourceLanguage = { language ->
                        selectedSourceLanguageText = language.name
                        selectedSourceLanguage = language.language
                        selectedTargetLanguage?.let {
                            translationViewModel.translateText(
                                sourceText = currentText,
                                sourceLanguage = language.language,
                                targetLanguage = it,
                            )
                        }
                    },
                    selectedSourceLanguage = selectedSourceLanguageText,
                    selectedTargetLanguage = selectedTargetLanguageText
                )
            }
        }

        when (translationState) {
            is TranslationUiState.Error -> {
                val error = (translationState as TranslationUiState.Error).message
                Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            }
            TranslationUiState.Idle -> Unit
            TranslationUiState.Loading -> Unit
            is TranslationUiState.Success -> {
                translatedText =
                    (translationState as TranslationUiState.Success).text
            }
        }

        Column(
            Modifier
                .padding(16.dp)
        ) {


                    AnimatedContent(modifier = Modifier
                        .padding(12.dp), targetState = imageTextState, label = "") {
                        when (it) {
                            is ImageTextUiState.Error -> {
                                val error = (imageTextState as ImageTextUiState.Error).message
                                Text(text = error, color = onPrimaryTextColor, fontSize = 18.sp)
                            }

                            ImageTextUiState.Idle -> Unit
                            ImageTextUiState.Loading -> Unit

                            is ImageTextUiState.Success -> {
                                Column {
                                    Text(
                                        text = "Recognized text",
                                        style = Typography.titleLarge
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    val recognizedText =
                                        (imageTextState as ImageTextUiState.Success).text
                                    translationViewModel.updateCurrentText(recognizedText)
                                    Text(text = recognizedText, color = onPrimaryTextColor, fontSize = 18.sp)
                                }
                            }
                        }
                    }

            Spacer(Modifier.height(20.dp))

            if (translatedText.isNotBlank())
                Text(
                    text = selectedTargetLanguageText,
                    style = Typography.headlineMedium
                )

            Spacer(Modifier.height(6.dp))

            androidx.compose.animation.AnimatedVisibility(translatedText.isNotBlank()) {
                    AnimatedContent(
                        modifier = Modifier
                            .padding(12.dp),
                        targetState = translatedText, label = "") {
                        Text(text = it, color = onPrimaryTextColor, fontSize = 18.sp)
                    }
                }
        }
    }
}


@Composable
fun TranslateOptions(
    modifier: Modifier = Modifier,
    availableSourceLanguages: List<Language>,
    availableTargetLanguages: List<Language>,
    selectedSourceLanguage: String,
    selectedTargetLanguage: String,
    onSelectedSourceLanguage: (Language) -> Unit,
    onSelectedTargetLanguage: (Language) -> Unit,
) {

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(100.dp),
        color = backgroundColor,
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            LanguageChip(
                availableLanguages = availableSourceLanguages,
                selectedLanguage = selectedSourceLanguage,
                onClick = { language ->
                    onSelectedSourceLanguage(language)
                }
            )

            Spacer(Modifier.width(12.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "arrow"
            )

            Spacer(Modifier.width(12.dp))

            LanguageChip(
                availableLanguages = availableTargetLanguages,
                selectedLanguage = selectedTargetLanguage,
                onClick = { language ->
                    onSelectedTargetLanguage(language)
                }
            )
        }
    }
}

@Composable
private fun LanguageChip(
    modifier: Modifier = Modifier,
    availableLanguages: List<Language>,
    selectedLanguage: String,
    onClick: (Language) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    SuggestionChip(
        shape = RoundedCornerShape(50.dp),
        modifier = modifier
            .wrapContentSize()
            .height(50.dp)
            .width(130.dp),
        label = {
            Text(
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = selectedLanguage
            )
        },
        onClick = { expanded = true }
    )

    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.wrapContentSize()
    ) {
        availableLanguages.forEach {
            DropdownMenuItem(
                text = { Text(it.name) },
                onClick = {
                    expanded = false
                    onClick(it)
                }
            )
        }
    }
}

@Preview
@Composable
fun PreviewTranslateOptions() {
    TranslateOptions(
        selectedSourceLanguage = "English",
        selectedTargetLanguage = "Ukrainian",
        availableSourceLanguages = listOf(
            Language(
                language = "UK",
                name = "Ukraine",
                supportsFormality = true
            ),
            Language(
                language = "EN",
                name = "Englisj",
                supportsFormality = true
            ),
            Language(
                language = "DE",
                name = "German",
                supportsFormality = true
            ),
        ),
        availableTargetLanguages = listOf(
            Language(
                language = "UK",
                name = "Ukraine",
                supportsFormality = true
            ),
            Language(
                language = "EN",
                name = "Englisj",
                supportsFormality = true
            ),
            Language(
                language = "DE",
                name = "German",
                supportsFormality = true
            ),
        ),
        onSelectedSourceLanguage = {},
        onSelectedTargetLanguage = {}
    )
}





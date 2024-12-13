package com.hamaro.rockettranslatenativeapp.ui.presentation.history

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.hamaro.rockettranslatenativeapp.domain.model.ImageFirestore
import com.hamaro.rockettranslatenativeapp.domain.model.UiState
import com.hamaro.rockettranslatenativeapp.ui.common.CustomTopBarWithReturn
import org.koin.androidx.compose.koinViewModel

@Composable
fun HistoryImageScreen(
    viewModel: ImageViewModel = koinViewModel(),
    navigateToHomeScreen: () -> Unit,
    onImageClick: (String) -> Unit
) {
    val images by viewModel.images.collectAsState()

    Scaffold(
        topBar = {
            CustomTopBarWithReturn(
                modifier = Modifier
                    .padding(WindowInsets.statusBars.asPaddingValues().calculateTopPadding()),
                title = "Image History",
                onClickBack = navigateToHomeScreen
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            when (images) {
                is UiState.Error -> {
                    Text(
                        text = (images as UiState.Error).message
                    )
                }

                UiState.Idle -> {
                    Text(text = "No images to display.")
                }

                UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Success -> {
                    GridContent(
                        modifier = Modifier,
                        images = (images as UiState.Success<List<ImageFirestore>>).data,
                        onImageClick = onImageClick
                    )
                }
            }
        }
    }
}
package com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hamaro.rockettranslatenativeapp.ui.common.SharedViewModel
import com.hamaro.rockettranslatenativeapp.ui.navigation.route.HomeDestination
import com.hamaro.rockettranslatenativeapp.ui.presentation.history.HistoryImageScreen
import com.hamaro.rockettranslatenativeapp.ui.presentation.image.ImageScreen

fun NavGraphBuilder.imageComposable(
    navigateHome: () -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = HomeDestination.Image.route
    ) {
        val imageState by remember { sharedViewModel.base64Image }

        ImageScreen(
            imageBase64 = imageState,
            navigateHome = navigateHome
        )
    }
}
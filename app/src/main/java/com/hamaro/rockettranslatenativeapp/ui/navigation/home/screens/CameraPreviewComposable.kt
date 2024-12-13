package com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hamaro.rockettranslatenativeapp.ui.navigation.route.HomeDestination
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.CameraPreview

fun NavGraphBuilder.cameraPreviewComposable(
    navigateToPhotoHistory : () -> Unit,
    navigateToPhoto : (String) -> Unit,
) {
    composable(
        route = HomeDestination.Camera.route
    ) {
        CameraPreview(
            navigateToPhotoHistory = navigateToPhotoHistory,
            navigateToPhoto = navigateToPhoto
        )
    }
}
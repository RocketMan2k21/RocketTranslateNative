package com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hamaro.rockettranslatenativeapp.ui.navigation.route.HomeDestination
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.CameraPermissionScreen

fun NavGraphBuilder.permissionComposable(
    navigateToCameraPreview: () -> Unit,
) {
    composable(
        route = HomeDestination.Permission.route
    ) {
        CameraPermissionScreen(
            onPermissionGranted = navigateToCameraPreview,
        )
    }
}
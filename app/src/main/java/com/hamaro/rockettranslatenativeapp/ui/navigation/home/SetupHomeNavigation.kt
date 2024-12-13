package com.hamaro.rockettranslatenativeapp.ui.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.ui.common.SharedViewModel
import com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens.cameraPreviewComposable
import com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens.historyComposable
import com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens.imageComposable
import com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens.permissionComposable
import com.hamaro.rockettranslatenativeapp.ui.navigation.route.HomeDestination
import org.koin.androidx.compose.koinViewModel

@Composable
fun SetupHomeNavigation(
    navController : NavHostController,
    startDestination : HomeDestination.Permission,
    sharedViewModel : SharedViewModel = koinViewModel()
) {

    val screens = remember(navController) {
        HomeScreens(navController = navController)
    }
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        permissionComposable (
           navigateToCameraPreview =  screens.cameraPreview,
        )
        cameraPreviewComposable(
            navigateToPhotoHistory = screens.history
        )
        historyComposable(
            navigateBack = screens.navigateBack,
            onImageClick = { base64 ->
                sharedViewModel.setBase64Image(base64)
                screens.image()
            }
        )
        imageComposable (navigateHome = screens.cameraPreview,
        sharedViewModel = sharedViewModel)
    }
}
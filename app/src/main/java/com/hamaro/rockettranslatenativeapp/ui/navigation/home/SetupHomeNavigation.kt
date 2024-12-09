package com.hamaro.rockettranslatenativeapp.ui.navigation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens.cameraPreviewComposable
import com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens.permissionComposable
import com.hamaro.rockettranslatenativeapp.ui.navigation.route.HomeDestination

@Composable
fun SetupHomeNavigation(
    navController : NavHostController,
    startDestination : HomeDestination.Permission
) {

    val screens = remember(navController) {
        HomeScreens(navController = navController)
    }
    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        permissionComposable (
           navigateToCameraPreview =  screens.cameraPreview
        )
        cameraPreviewComposable {

        }
    }
}
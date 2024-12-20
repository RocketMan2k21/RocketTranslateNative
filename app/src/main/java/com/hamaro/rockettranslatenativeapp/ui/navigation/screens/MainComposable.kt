package com.hamaro.rockettranslatenativeapp.ui.navigation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.ui.navigation.home.SetupHomeNavigation
import com.hamaro.rockettranslatenativeapp.ui.navigation.route.HomeDestination
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.CameraPermissionScreen

fun NavGraphBuilder.mainComposable() {
    composable(
        route = MainViewModel.Destination.Main.route
    ) {
        val navController = rememberNavController()
        SetupHomeNavigation(
            navController = navController,
            startDestination = HomeDestination.Permission
        )
    }
}
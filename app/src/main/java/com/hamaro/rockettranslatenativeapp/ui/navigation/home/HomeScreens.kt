package com.hamaro.rockettranslatenativeapp.ui.navigation.home

import androidx.navigation.NavHostController
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.ui.navigation.route.HomeDestination

class HomeScreens(navController: NavHostController) {

    val cameraPreview: () -> Unit = {
        navController.navigate(HomeDestination.Camera.route){
            popUpTo(0)
        }
    }

    val history: () -> Unit = {
        navController.navigate(HomeDestination.History.route)
    }

    val image : () -> Unit = {
        navController.navigate("${HomeDestination.Image}")
    }

    val navigateBack : () -> Unit = {
        navController.popBackStack()
    }
}
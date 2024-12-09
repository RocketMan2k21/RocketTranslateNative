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
}
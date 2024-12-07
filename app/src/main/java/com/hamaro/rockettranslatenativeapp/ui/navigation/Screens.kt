package com.hamaro.rockettranslatenativeapp.ui.navigation

import androidx.navigation.NavHostController
import com.hamaro.rockettranslatenativeapp.MainViewModel

class Screens(navController: NavHostController) {
    val auth: () -> Unit = {
        navController.navigate(MainViewModel.Destination.Auth.route){
            popUpTo(0)
        }
    }
}
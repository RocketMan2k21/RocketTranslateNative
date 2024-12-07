package com.hamaro.rockettranslatenativeapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.ui.navigation.screens.authComposable
import com.hamaro.rockettranslatenativeapp.ui.navigation.screens.mainComposable

@Composable
fun SetupNavigation(
    navController: NavHostController,
    startDestination : MainViewModel.Destination
) {

    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(
        navController = navController,
        startDestination = startDestination.route
    ) {
        authComposable(
            navController
        )
        mainComposable(

        )
    }
}
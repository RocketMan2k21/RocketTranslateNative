package com.hamaro.rockettranslatenativeapp.ui.navigation.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.auth.AuthScreen
import com.hamaro.rockettranslatenativeapp.ui.presentation.auth.SignUpScreen

fun NavGraphBuilder.signUpComposable(
    navController: NavHostController,
    navigateToAuth : () -> Unit
) {
    composable(MainViewModel.Destination.SignUp.route) {
        SignUpScreen(
            onSuccessSignIn = {
                navController.navigate(MainViewModel.Destination.Main.route) {
                    popUpTo(0)
                }
            },
            navigateToAuthScreen = navigateToAuth
        )
    }
}
package com.hamaro.rockettranslatenativeapp.ui.navigation.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.auth.AuthScreen

fun NavGraphBuilder.authComposable(
    navController: NavHostController,
    navigateToSignUp : () -> Unit
) {
    composable(MainViewModel.Destination.Auth.route) {
        AuthScreen(
            onSuccessSignIn = {
                navController.navigate(MainViewModel.Destination.Main.route) {
                    popUpTo(0)
                }
            },
            navigateToSignUpPage = navigateToSignUp
        )
    }
}
package com.hamaro.rockettranslatenativeapp.ui.navigation.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.hamaro.rockettranslatenativeapp.MainViewModel

fun NavGraphBuilder.mainComposable(

) {
    composable(
        route = MainViewModel.Destination.Main.route
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Hello World")
        }
    }
}
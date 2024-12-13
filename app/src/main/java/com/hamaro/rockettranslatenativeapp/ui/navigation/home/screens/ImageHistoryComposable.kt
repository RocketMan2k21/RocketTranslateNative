package com.hamaro.rockettranslatenativeapp.ui.navigation.home.screens

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.hamaro.rockettranslatenativeapp.domain.model.ImageFirestore
import com.hamaro.rockettranslatenativeapp.ui.navigation.route.HomeDestination
import com.hamaro.rockettranslatenativeapp.ui.presentation.history.HistoryImageScreen

fun NavGraphBuilder.historyComposable(
    navigateBack : () -> Unit,
    onImageClick : (String) -> Unit
) {
    composable(
        route = HomeDestination.History.route
    ) {
        HistoryImageScreen(
            navigateToHomeScreen = navigateBack,
            onImageClick = onImageClick
        )
    }
}
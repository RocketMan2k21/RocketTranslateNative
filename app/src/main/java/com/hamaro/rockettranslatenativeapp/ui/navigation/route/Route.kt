package com.hamaro.rockettranslatenativeapp.ui.navigation.route

sealed class HomeDestination(val route : String){
    data object Permission : HomeDestination("permission")
    data object Camera : HomeDestination("camera")
    data object History : HomeDestination("history")
    data object Image : HomeDestination("image")

    companion object {
        const val IMAGE_ARGUMENT_KEY = "base64"
    }
}
package com.hamaro.rockettranslatenativeapp.ui.theme

import android.graphics.Color.parseColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val onPrimaryTextColor : Color
    @Composable
    get() = if(!isSystemInDarkTheme()) Color.Black
        else Color.White

val cameraPreviewIconColor : Color
    get() = Color.White

val backGroundForCameraPreviewBounds : Color
    get() =  Color(0x80000000)

val textCameraColor : Color
    @Composable
    get() = if(!isSystemInDarkTheme()) Color.White
    else Color.White

val backgroundColor : Color
    @Composable
    get() = if(!isSystemInDarkTheme()) Color.White
    else grayBlack

val borderStrokeColor : Color
    @Composable
    get() = if(isSystemInDarkTheme()) Color.White
    else grayBlack
val grayBlack = Color(0xff495057)
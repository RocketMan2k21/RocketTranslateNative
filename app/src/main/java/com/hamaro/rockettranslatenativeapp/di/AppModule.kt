package com.hamaro.rockettranslatenativeapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.data.remote.repository.AndroidTextRecognizer
import com.hamaro.rockettranslatenativeapp.data.remote.repository.AuthServiceImpl
import com.hamaro.rockettranslatenativeapp.domain.AuthService
import com.hamaro.rockettranslatenativeapp.domain.TextRecognizer
import com.hamaro.rockettranslatenativeapp.ui.presentation.auth.AuthViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TextRecognizerViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single<AuthService>{ AuthServiceImpl(auth = FirebaseAuth.getInstance()) }
    single<TextRecognizer>{ AndroidTextRecognizer(functions = FirebaseFunctions.getInstance()) }
    factory { AuthViewModel(authService = get()) }
    factory { MainViewModel(authViewModel = get()) }
    factory { TextRecognizerViewModel(textRecognizer = get()) }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}
package com.hamaro.rockettranslatenativeapp.di

import com.google.firebase.auth.FirebaseAuth
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.data.repository.AuthServiceImpl
import com.hamaro.rockettranslatenativeapp.domain.AuthService
import com.hamaro.rockettranslatenativeapp.ui.presentation.auth.AuthViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single<AuthService>{AuthServiceImpl(auth = FirebaseAuth.getInstance())}
    factory { AuthViewModel(authService = get()) }
    factory { MainViewModel(authViewModel = get()) }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}
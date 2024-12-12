package com.hamaro.rockettranslatenativeapp.di

import com.google.firebase.auth.FirebaseAuth
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.data.remote.repository.AuthServiceImpl
import com.hamaro.rockettranslatenativeapp.data.remote.repository.ImageRepositoryImpl
import com.hamaro.rockettranslatenativeapp.data.remote.repository.LocalTextRecognizer
import com.hamaro.rockettranslatenativeapp.data.remote.repository.TextTranslationRepositoryImpl
import com.hamaro.rockettranslatenativeapp.domain.AuthService
import com.hamaro.rockettranslatenativeapp.domain.ImageRepository
import com.hamaro.rockettranslatenativeapp.domain.TextRecognizer
import com.hamaro.rockettranslatenativeapp.domain.TextTranslationService
import com.hamaro.rockettranslatenativeapp.domain.TranslationApiRepository
import com.hamaro.rockettranslatenativeapp.ui.presentation.auth.AuthViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TextRecognizerViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TranslationViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.history.ImageViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit

val appModule = module {
    single<AuthService>{ AuthServiceImpl(auth = FirebaseAuth.getInstance()) }
    single<TextRecognizer>{ LocalTextRecognizer() }
    single<ImageRepository>{ ImageRepositoryImpl(authService = get()) }
    single<TextTranslationService> { TextTranslationRepositoryImpl(translationApi = get()) }
    single<TranslationApiRepository> { get<Retrofit>().create(TranslationApiRepository::class.java)}
    factory { AuthViewModel(authService = get()) }
    factory { MainViewModel(authViewModel = get()) }
    factory { TextRecognizerViewModel(textRecognizer = get()) }
    factory { TranslationViewModel(translationService = get()) }
    factory { ImageViewModel(imageRepository = get()) }
}

fun initializeKoin() {
    startKoin {
        modules(listOf(apiModule, appModule))
    }
}
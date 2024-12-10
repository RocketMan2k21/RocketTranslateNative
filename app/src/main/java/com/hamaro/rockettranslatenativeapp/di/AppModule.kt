package com.hamaro.rockettranslatenativeapp.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.hamaro.rockettranslatenativeapp.MainViewModel
import com.hamaro.rockettranslatenativeapp.data.networking.interceptors.OkHttpInterceptor
import com.hamaro.rockettranslatenativeapp.data.remote.repository.AndroidTextRecognizer
import com.hamaro.rockettranslatenativeapp.data.remote.repository.AuthServiceImpl
import com.hamaro.rockettranslatenativeapp.data.remote.repository.LocalTextRecognizer
import com.hamaro.rockettranslatenativeapp.data.remote.repository.TextTranslationRepositoryImpl
import com.hamaro.rockettranslatenativeapp.domain.AuthService
import com.hamaro.rockettranslatenativeapp.domain.TextRecognizer
import com.hamaro.rockettranslatenativeapp.domain.TextTranslationService
import com.hamaro.rockettranslatenativeapp.domain.TranslationApiRepository
import com.hamaro.rockettranslatenativeapp.ui.presentation.auth.AuthViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TextRecognizerViewModel
import com.hamaro.rockettranslatenativeapp.ui.presentation.camera.TranslationViewModel
import okhttp3.OkHttpClient
import org.koin.core.context.startKoin
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {
    single<OkHttpClient> {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(OkHttpInterceptor())
            .build()
    }
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl("https://api-free.deepl.com/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    single<AuthService>{ AuthServiceImpl(auth = FirebaseAuth.getInstance()) }
    single<TextRecognizer>{ LocalTextRecognizer() }
    single<TextTranslationService> { TextTranslationRepositoryImpl(translationApi = get()) }
    single<TranslationApiRepository> { get<Retrofit>().create(TranslationApiRepository::class.java)}
    factory { AuthViewModel(authService = get()) }
    factory { MainViewModel(authViewModel = get()) }
    factory { TextRecognizerViewModel(textRecognizer = get()) }
    factory { TranslationViewModel(translationService = get()) }
}

fun initializeKoin() {
    startKoin {
        modules(appModule)
    }
}
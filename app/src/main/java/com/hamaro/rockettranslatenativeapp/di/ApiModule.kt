package com.hamaro.rockettranslatenativeapp.di

import com.hamaro.rockettranslatenativeapp.data.networking.interceptors.OkHttpInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val apiModule = module {
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
}
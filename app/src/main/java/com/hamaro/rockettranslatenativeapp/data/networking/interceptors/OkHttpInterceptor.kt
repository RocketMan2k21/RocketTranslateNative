package com.hamaro.rockettranslatenativeapp.data.networking.interceptors

import com.hamaro.rockettranslatenativeapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class OkHttpInterceptor :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        request
            .addHeader(
                "Authorization",
                "DeepL-Auth-Key ${BuildConfig.TRANSLATION_API_KEY}"
            )

        Headers.HeaderMap.forEach { (key, value) ->
            request.addHeader(key, value)
        }

        return chain.proceed(request.build())
    }

    internal object Headers {
        val HeaderMap = hashMapOf(
            "accept" to "application/json",
            "Content-Type" to "application/x-www-form-urlencoded",
        )
    }
}
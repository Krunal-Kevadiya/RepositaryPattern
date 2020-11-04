package com.example.ownrepositarypatternsample.di.interceptor

import android.content.Context
import com.example.ownrepositarypatternsample.BuildConfig
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Response

internal class RequestInterceptor(
    private val context: Context,
    private val argUrl: String
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val httpUrl: HttpUrl? = argUrl.toHttpUrlOrNull()
        var originalRequest = chain.request()

        //Change url when need
        if (httpUrl != null) {
            val newUrl = originalRequest.url.newBuilder()
                .scheme(httpUrl.scheme)
                .host(httpUrl.host)

            originalRequest = originalRequest.newBuilder()
                .url(newUrl.build())
                .build()
        }

        val originalUrl = originalRequest.url
        val url = originalUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.TMDB_API_KEY)
                .build()

        val requestBuilder = originalRequest.newBuilder().url(url)
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}

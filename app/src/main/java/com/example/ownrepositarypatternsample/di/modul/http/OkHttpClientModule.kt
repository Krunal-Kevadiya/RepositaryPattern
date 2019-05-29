package com.example.ownrepositarypatternsample.di.modul.http

import android.content.Context
import com.example.ownrepositarypatternsample.di.interceptor.ConnectivityInterceptor
import com.example.ownrepositarypatternsample.di.interceptor.RequestInterceptor
import com.example.ownrepositarypatternsample.di.modul.Property
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import java.io.File
import java.util.concurrent.TimeUnit

val okHttpClientModule = module {
    single {
        val file = File(get<Context>().cacheDir, "restapi-cache")
        file.mkdirs()
        file
    }
    single {
        Cache(get<File>(), 10 * 1000 * 1000) // 10MB cache file
    }
    single {
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }
    single {
        val timeout = 30
        OkHttpClient.Builder()
            .cache(get<Cache>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .addInterceptor(ConnectivityInterceptor(get()))
            .addInterceptor(RequestInterceptor(get(), getProperty(Property.BASE_URL)))
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .build()
    }
}

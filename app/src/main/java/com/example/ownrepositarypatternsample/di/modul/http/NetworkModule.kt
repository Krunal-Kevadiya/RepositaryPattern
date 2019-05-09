package com.example.ownrepositarypatternsample.di.modul.http

import com.example.ownrepositarypatternsample.BuildConfig
import com.example.ownrepositarypatternsample.data.remote.adapter.LiveDataCallAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        BuildConfig.API_BASE_URL
    }
    single {
        GsonBuilder().create()
    }
    single {
        GsonConverterFactory.create(get<Gson>())
    }
    single {
        Retrofit.Builder()
            .baseUrl(get<String>())
            .client(get<OkHttpClient>() )
            .addConverterFactory(get<GsonConverterFactory>())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }
}

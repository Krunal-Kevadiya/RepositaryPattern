package com.example.ownrepositarypatternsample.di.modul

import android.content.Context
import androidx.annotation.NonNull
import com.example.ownrepositarypatternsample.BuildConfig
import com.example.ownrepositarypatternsample.data.remote.adapter.LiveDataCallAdapterFactory
import com.example.ownrepositarypatternsample.data.remote.service.MovieService
import com.example.ownrepositarypatternsample.data.remote.service.PeopleService
import com.example.ownrepositarypatternsample.data.remote.service.TheDiscoverService
import com.example.ownrepositarypatternsample.data.remote.service.TvService
import com.example.ownrepositarypatternsample.di.interceptor.ConnectivityInterceptor
import com.example.ownrepositarypatternsample.di.interceptor.RequestInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [OkHttpClientModule::class])
class NetworkModule {
    @Singleton
    @Provides
    fun provideBaseUrlString(): String {
        return BuildConfig.API_BASE_URL
    }

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().create()
    }

    @Singleton
    @Provides
    fun gsonConverterFactory(gson: Gson): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideBaseRetrofit(
        context: Context,
        baseUrl: String,
        okHttpClient: OkHttpClient.Builder,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        okHttpClient.addInterceptor(ConnectivityInterceptor(context))
            .addInterceptor(RequestInterceptor(context))
            .addNetworkInterceptor(StethoInterceptor())

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideDiscoverService(@NonNull retrofit: Retrofit): TheDiscoverService {
        return retrofit.create(TheDiscoverService::class.java)
    }

    @Provides
    @Singleton
    fun providePeopleService(@NonNull retrofit: Retrofit): PeopleService {
        return retrofit.create(PeopleService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieService(@NonNull retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }

    @Provides
    @Singleton
    fun provideTvService(@NonNull retrofit: Retrofit): TvService {
        return retrofit.create(TvService::class.java)
    }
}

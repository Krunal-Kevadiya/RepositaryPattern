package com.example.ownrepositarypatternsample

import android.app.Application
import com.example.ownrepositarypatternsample.di.modul.*
import com.example.ownrepositarypatternsample.di.modul.http.apiModule
import com.example.ownrepositarypatternsample.di.modul.http.networkModule
import com.example.ownrepositarypatternsample.di.modul.http.okHttpClientModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class TheApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@TheApplication)
            modules(
                listOf(
                    appModule, okHttpClientModule, networkModule,
                    apiModule, persistenceModule, repositoryModule, viewModelModule
                )
            )
            properties(mapOf(Property.BASE_URL to BuildConfig.API_BASE_URL))
        }
    }
}

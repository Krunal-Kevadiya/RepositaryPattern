package com.example.ownrepositarypatternsample

import com.example.ownrepositarypatternsample.di.component.DaggerAppComponent
import com.example.ownrepositarypatternsample.di.modul.AppModule
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class TheApplication: DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Stetho.initializeWithDefaults(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .appModule(AppModule(this))
            .build()
    }
}

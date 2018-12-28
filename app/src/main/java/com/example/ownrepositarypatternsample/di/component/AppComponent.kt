package com.example.ownrepositarypatternsample.di.component

import android.app.Application
import com.example.ownrepositarypatternsample.di.modul.AppModule
import com.example.ownrepositarypatternsample.di.modul.NetworkModule
import com.example.ownrepositarypatternsample.di.modul.PersistenceModule
import com.example.ownrepositarypatternsample.di.modul.activity.ActivityModule
import com.example.ownrepositarypatternsample.di.modul.viewmodel.ViewModelModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    ViewModelModule::class,
    NetworkModule::class,
    PersistenceModule::class
])
interface AppComponent : AndroidInjector<DaggerApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun appModule(appModule: AppModule): Builder
        fun build(): AppComponent
    }
}

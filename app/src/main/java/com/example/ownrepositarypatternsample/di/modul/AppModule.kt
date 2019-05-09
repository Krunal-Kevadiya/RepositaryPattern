package com.example.ownrepositarypatternsample.di.modul

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    single {
        androidContext().resources
    }
}
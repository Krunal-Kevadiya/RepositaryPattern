package com.example.ownrepositarypatternsample.di.modul

import androidx.room.Room
import com.example.ownrepositarypatternsample.data.local.AppDatabase
import com.example.ownrepositarypatternsample.data.local.DatabaseAnnotation
import org.koin.dsl.module

val persistenceModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, DatabaseAnnotation.DATABASE_NAME)
            .allowMainThreadQueries()
            .build()
    }
    single {
        get<AppDatabase>().movieDao()
    }
    single {
        get<AppDatabase>().tvDao()
    }
    single {
        get<AppDatabase>().peopleDao()
    }
}

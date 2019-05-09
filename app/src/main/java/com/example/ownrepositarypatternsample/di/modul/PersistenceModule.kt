package com.example.ownrepositarypatternsample.di.modul

import androidx.room.Room
import com.example.ownrepositarypatternsample.data.local.AppDatabase
import org.koin.dsl.module

val persistenceModule = module {
    single {
        Room.databaseBuilder(get(), AppDatabase::class.java, "TheMovies.db").allowMainThreadQueries().build()
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

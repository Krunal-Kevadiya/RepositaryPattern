package com.example.ownrepositarypatternsample.di.modul

import com.example.ownrepositarypatternsample.data.repository.DiscoverRepository
import com.example.ownrepositarypatternsample.data.repository.MovieRepository
import com.example.ownrepositarypatternsample.data.repository.PeopleRepository
import com.example.ownrepositarypatternsample.data.repository.TvRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        DiscoverRepository(get(), get(), get())
    }
    single {
        MovieRepository(get(), get())
    }
    single {
        PeopleRepository(get(), get())
    }
    single {
        TvRepository(get(), get())
    }
}
package com.example.ownrepositarypatternsample.di.modul

import com.example.ownrepositarypatternsample.data.repository.DiscoverRepository
import com.example.ownrepositarypatternsample.data.repository.MovieRepository
import com.example.ownrepositarypatternsample.data.repository.PeopleRepository
import com.example.ownrepositarypatternsample.data.repository.TvRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val repositoryModule = module {
    factory {
        val job = SupervisorJob()
        CoroutineScope(job + Dispatchers.IO)
    }
    single {
        DiscoverRepository(get(), get(), get(), get())
    }
    single {
        MovieRepository(get(), get(), get())
    }
    single {
        PeopleRepository(get(), get(), get())
    }
    single {
        TvRepository(get(), get(), get())
    }
}
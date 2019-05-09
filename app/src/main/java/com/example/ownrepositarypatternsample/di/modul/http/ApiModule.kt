package com.example.ownrepositarypatternsample.di.modul.http

import com.example.ownrepositarypatternsample.data.remote.service.MovieService
import com.example.ownrepositarypatternsample.data.remote.service.PeopleService
import com.example.ownrepositarypatternsample.data.remote.service.TheDiscoverService
import com.example.ownrepositarypatternsample.data.remote.service.TvService
import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {
    single {
        get<Retrofit>().create(TheDiscoverService::class.java)
    }
    single {
        get<Retrofit>().create(PeopleService::class.java)
    }
    single {
        get<Retrofit>().create(MovieService::class.java)
    }
    single {
        get<Retrofit>().create(TvService::class.java)
    }
}
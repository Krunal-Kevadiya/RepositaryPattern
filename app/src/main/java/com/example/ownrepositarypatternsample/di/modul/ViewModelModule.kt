package com.example.ownrepositarypatternsample.di.modul

import com.example.ownrepositarypatternsample.ui.main.MainActivity
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.movie.detail.MovieDetailViewModel
import com.example.ownrepositarypatternsample.ui.person.detail.PersonDetailViewModel
import com.example.ownrepositarypatternsample.ui.tv.detail.TvDetailViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    scope(named<MainActivity>()) {
        scoped { MainViewModel(get(), get()) }
    }
    scope(named<MainActivity>()) {
        scoped { MovieDetailViewModel(get()) }
    }
    scope(named<MainActivity>()) {
        scoped { TvDetailViewModel(get()) }
    }
    scope(named<MainActivity>()) {
        scoped { PersonDetailViewModel(get()) }
    }
}

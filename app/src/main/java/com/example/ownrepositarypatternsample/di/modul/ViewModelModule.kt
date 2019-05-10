package com.example.ownrepositarypatternsample.di.modul

import com.example.ownrepositarypatternsample.ui.main.MainActivity
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.movie.detail.MovieDetailActivity
import com.example.ownrepositarypatternsample.ui.movie.detail.MovieDetailViewModel
import com.example.ownrepositarypatternsample.ui.person.detail.PersonDetailActivity
import com.example.ownrepositarypatternsample.ui.person.detail.PersonDetailViewModel
import com.example.ownrepositarypatternsample.ui.tv.detail.TvDetailActivity
import com.example.ownrepositarypatternsample.ui.tv.detail.TvDetailViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    scope(named<MainActivity>()) {//MovieListFragment, PeopleListFragment, TvListFragment
        scoped { MainViewModel(get(), get()) }
    }
    scope(named<MovieDetailActivity>()) {
        scoped { MovieDetailViewModel(get()) }
    }
    scope(named<TvDetailActivity>()) {
        scoped { TvDetailViewModel(get()) }
    }
    scope(named<PersonDetailActivity>()) {
        scoped { PersonDetailViewModel(get()) }
    }
}

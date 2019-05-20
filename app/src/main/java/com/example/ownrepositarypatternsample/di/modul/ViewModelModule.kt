package com.example.ownrepositarypatternsample.di.modul

import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.movie.detail.MovieDetailViewModel
import com.example.ownrepositarypatternsample.ui.person.detail.PersonDetailViewModel
import com.example.ownrepositarypatternsample.ui.tv.detail.TvDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel(get(), get())
    }
    viewModel {
        MovieDetailViewModel(get())
    }
    viewModel {
        TvDetailViewModel(get())
    }
    viewModel {
        PersonDetailViewModel(get())
    }
}

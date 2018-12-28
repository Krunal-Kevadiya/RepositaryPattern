package com.example.ownrepositarypatternsample.di.modul.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ownrepositarypatternsample.di.scope.ViewModelKey
import com.example.ownrepositarypatternsample.di.factory.ViewModelFactory
import com.example.ownrepositarypatternsample.ui.main.MainViewModel
import com.example.ownrepositarypatternsample.ui.movie.detail.MovieDetailViewModel
import com.example.ownrepositarypatternsample.ui.person.detail.PersonDetailViewModel
import com.example.ownrepositarypatternsample.ui.tv.detail.TvDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainActivityViewModels(mainActivityViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    internal abstract fun bindMovieDetailViewModel(movieDetailViewModel: MovieDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TvDetailViewModel::class)
    internal abstract fun bindTvDetailViewModel(tvDetailViewModel: TvDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonDetailViewModel::class)
    internal abstract fun bindPersonDetailViewModel(personDetailViewModel: PersonDetailViewModel): ViewModel
}

package com.example.ownrepositarypatternsample.di.modul.fragment

import com.example.ownrepositarypatternsample.di.scope.PerFragment
import com.example.ownrepositarypatternsample.ui.movie.MovieListFragment
import com.example.ownrepositarypatternsample.ui.person.PersonListFragment
import com.example.ownrepositarypatternsample.ui.tv.TvListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityFragmentModule {

    @PerFragment
    @ContributesAndroidInjector
    abstract fun contributeMovieListFragment(): MovieListFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun contributeTvListFragment(): TvListFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun contributePersonListFragment(): PersonListFragment
}

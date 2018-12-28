package com.example.ownrepositarypatternsample.di.modul.activity

import com.example.ownrepositarypatternsample.di.modul.fragment.MainActivityFragmentModule
import com.example.ownrepositarypatternsample.di.scope.PerActivity
import com.example.ownrepositarypatternsample.di.scope.PerFragment
import com.example.ownrepositarypatternsample.ui.main.MainActivity
import com.example.ownrepositarypatternsample.ui.movie.MovieListFragment
import com.example.ownrepositarypatternsample.ui.movie.detail.MovieDetailActivity
import com.example.ownrepositarypatternsample.ui.person.PersonListFragment
import com.example.ownrepositarypatternsample.ui.person.detail.PersonDetailActivity
import com.example.ownrepositarypatternsample.ui.tv.TvListFragment
import com.example.ownrepositarypatternsample.ui.tv.detail.TvDetailActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {
    @PerActivity
    @ContributesAndroidInjector//(modules = [MainActivityFragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @PerActivity
    @ContributesAndroidInjector
    internal abstract fun contributeMovieDetailActivity(): MovieDetailActivity

    @PerActivity
    @ContributesAndroidInjector
    internal abstract fun contributeTvDetailActivity(): TvDetailActivity

    @PerActivity
    @ContributesAndroidInjector
    internal abstract fun contributePersonDetailActivity(): PersonDetailActivity

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

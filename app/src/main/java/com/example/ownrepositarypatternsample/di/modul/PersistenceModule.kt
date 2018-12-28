package com.example.ownrepositarypatternsample.di.modul

import android.app.Application
import androidx.room.Room
import androidx.annotation.NonNull
import com.example.ownrepositarypatternsample.data.local.AppDatabase
import com.example.ownrepositarypatternsample.data.local.dao.MovieDao
import com.example.ownrepositarypatternsample.data.local.dao.PeopleDao
import com.example.ownrepositarypatternsample.data.local.dao.TvDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {
    @Provides
    @Singleton
    fun provideDatabase(@NonNull application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "TheMovies.db").allowMainThreadQueries().build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(@NonNull database: AppDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    @Singleton
    fun provideTvDao(@NonNull database: AppDatabase): TvDao {
        return database.tvDao()
    }

    @Provides
    @Singleton
    fun providePeopleDao(@NonNull database: AppDatabase): PeopleDao {
        return database.peopleDao()
    }
}

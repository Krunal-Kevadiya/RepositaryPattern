package com.example.ownrepositarypatternsample.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ownrepositarypatternsample.data.local.converters.*
import com.example.ownrepositarypatternsample.data.local.dao.MovieDao
import com.example.ownrepositarypatternsample.data.local.dao.PeopleDao
import com.example.ownrepositarypatternsample.data.local.dao.TvDao
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.local.entity.Person
import com.example.ownrepositarypatternsample.data.local.entity.Tv

@Database(entities = [Movie::class, Tv::class, Person::class], version = 3, exportSchema = false)
@TypeConverters(value = [StringListConverter::class, IntegerListConverter::class,
    KeywordListConverter::class, VideoListConverter::class, ReviewListConverter::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun tvDao(): TvDao
    abstract fun peopleDao(): PeopleDao
}
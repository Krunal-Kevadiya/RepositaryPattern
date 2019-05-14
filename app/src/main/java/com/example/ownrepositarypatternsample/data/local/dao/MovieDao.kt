package com.example.ownrepositarypatternsample.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ownrepositarypatternsample.data.local.DatabaseAnnotation
import com.example.ownrepositarypatternsample.data.local.entity.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovieList(movies: List<Movie>)

    @Update
    fun updateMovie(movie: Movie)

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_MOVIE} WHERE ${DatabaseAnnotation.ID} = :id_")
    fun getMovie(id_: Int): Movie

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_MOVIE} WHERE ${DatabaseAnnotation.PAGE} = :page_")
    fun getMovieList(page_: Int): LiveData<List<Movie>>
}

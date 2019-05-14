package com.example.ownrepositarypatternsample.data.local.dao

import androidx.room.*
import com.example.ownrepositarypatternsample.data.local.DatabaseAnnotation
import com.example.ownrepositarypatternsample.data.local.entity.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movies: List<Movie>)

    @Update
    suspend fun updateMovie(movie: Movie)

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_MOVIE} WHERE ${DatabaseAnnotation.ID} = :id_")
    suspend fun getMovie(id_: Int): Movie

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_MOVIE} WHERE ${DatabaseAnnotation.PAGE} = :page_")
    suspend fun getMovieList(page_: Int): List<Movie>
}

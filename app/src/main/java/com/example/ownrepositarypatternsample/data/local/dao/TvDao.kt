package com.example.ownrepositarypatternsample.data.local.dao

import androidx.room.*
import com.example.ownrepositarypatternsample.data.local.DatabaseAnnotation
import com.example.ownrepositarypatternsample.data.local.entity.Tv

@Dao
interface TvDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTv(tvs: List<Tv>)

    @Update
    suspend fun updateTv(tv: Tv)

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_TV} WHERE ${DatabaseAnnotation.ID} = :id_")
    suspend fun getTv(id_: Int): Tv

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_TV} WHERE ${DatabaseAnnotation.PAGE} = :page_")
    suspend fun getTvList(page_: Int) : List<Tv>
}

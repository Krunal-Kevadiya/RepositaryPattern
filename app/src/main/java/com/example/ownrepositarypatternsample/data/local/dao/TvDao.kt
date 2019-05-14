package com.example.ownrepositarypatternsample.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ownrepositarypatternsample.data.local.DatabaseAnnotation
import com.example.ownrepositarypatternsample.data.local.entity.Tv

@Dao
interface TvDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTv(tvs: List<Tv>)

    @Update
    fun updateTv(tv: Tv)

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_TV} WHERE ${DatabaseAnnotation.ID} = :id_")
    fun getTv(id_: Int): Tv

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_TV} WHERE ${DatabaseAnnotation.PAGE} = :page_")
    fun getTvList(page_: Int) : LiveData<List<Tv>>
}

package com.example.ownrepositarypatternsample.data.local.dao

import androidx.room.*
import com.example.ownrepositarypatternsample.data.local.DatabaseAnnotation
import com.example.ownrepositarypatternsample.data.local.entity.People

@Dao
interface PeopleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPeople(people: List<People>)

    @Update
    suspend fun updatePerson(people: People)

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_PEOPLE} WHERE ${DatabaseAnnotation.ID} = :id_")
    suspend fun getPerson(id_: Int): People

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_PEOPLE} WHERE ${DatabaseAnnotation.PAGE} = :page_")
    suspend fun getPeople(page_: Int): List<People>
}
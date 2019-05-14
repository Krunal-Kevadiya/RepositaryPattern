package com.example.ownrepositarypatternsample.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ownrepositarypatternsample.data.local.DatabaseAnnotation
import com.example.ownrepositarypatternsample.data.local.entity.People

@Dao
interface PeopleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPeople(people: List<People>)

    @Update
    fun updatePerson(people: People)

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_PEOPLE} WHERE ${DatabaseAnnotation.ID} = :id_")
    fun getPerson(id_: Int): People

    @Query("SELECT * FROM ${DatabaseAnnotation.TABLE_PEOPLE} WHERE ${DatabaseAnnotation.PAGE} = :page_")
    fun getPeople(page_: Int): LiveData<List<People>>
}
package com.example.ownrepositarypatternsample.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import android.os.Parcelable
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "People", primaryKeys = ["id"])
data class Person(
    var page: Int,
    @Embedded var personDetail: PersonDetail? = null,
    val profile_path: String?,
    val adult: Boolean,
    val id: Int,
    val name: String,
    val popularity: Float
) : Parcelable
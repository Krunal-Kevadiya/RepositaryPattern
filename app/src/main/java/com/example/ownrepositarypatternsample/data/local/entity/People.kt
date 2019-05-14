package com.example.ownrepositarypatternsample.data.local.entity

import androidx.room.Embedded
import androidx.room.Entity
import android.os.Parcelable
import androidx.room.ColumnInfo
import com.example.ownrepositarypatternsample.data.local.DatabaseAnnotation
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = DatabaseAnnotation.TABLE_PEOPLE,
    primaryKeys = [DatabaseAnnotation.ID]
)
data class People(
    @SerializedName("page") @ColumnInfo(name = DatabaseAnnotation.PAGE) var page: Int,
    @SerializedName("profile_path") @ColumnInfo(name = DatabaseAnnotation.PROFILE_PATH) val profilePath: String?,
    @SerializedName("adult") @ColumnInfo(name = DatabaseAnnotation.ADULT) val adult: Boolean,
    @SerializedName("id") @ColumnInfo(name = DatabaseAnnotation.ID) val id: Int,
    @SerializedName("name") @ColumnInfo(name = DatabaseAnnotation.NAME) val name: String,
    @SerializedName("popularity") @ColumnInfo(name = DatabaseAnnotation.POPULARITY) val popularity: Float,
    @SerializedName("personDetail") @Embedded var personDetail: PersonDetail? = null
) : Parcelable
package com.example.ownrepositarypatternsample.data.local.entity

import androidx.room.Entity
import android.os.Parcelable
import androidx.room.ColumnInfo
import com.example.ownrepositarypatternsample.data.local.DatabaseAnnotation
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = DatabaseAnnotation.TABLE_TV,
    primaryKeys = [DatabaseAnnotation.ID]
)
data class Tv(
    @SerializedName("id") @ColumnInfo(name = DatabaseAnnotation.ID) val id: Int,
    @SerializedName("page") @ColumnInfo(name = DatabaseAnnotation.PAGE) var page: Int,
    @SerializedName("keywords") @ColumnInfo(name = DatabaseAnnotation.KEYWORDS) var keywords: List<Keyword>? = ArrayList(),
    @SerializedName("videos") @ColumnInfo(name = DatabaseAnnotation.VIDEOS) var videos: List<Video>? = ArrayList(),
    @SerializedName("reviews") @ColumnInfo(name = DatabaseAnnotation.REVIEWS) var reviews: List<Review>? = ArrayList(),
    @SerializedName("poster_path") @ColumnInfo(name = DatabaseAnnotation.POSTER_PATH) val posterPath: String?,
    @SerializedName("popularity") @ColumnInfo(name = DatabaseAnnotation.POPULARITY) val popularity: Float,
    @SerializedName("backdrop_path") @ColumnInfo(name = DatabaseAnnotation.BACKDROP_PATH) val backdropPath: String?,
    @SerializedName("vote_average") @ColumnInfo(name = DatabaseAnnotation.VOTE_AVERAGE) val voteAverage: Float,
    @SerializedName("overview") @ColumnInfo(name = DatabaseAnnotation.OVERVIEW) val overview: String,
    @SerializedName("first_air_date") @ColumnInfo(name = DatabaseAnnotation.FIRST_AIR_DATE) val firstAirDate: String,
    @SerializedName("origin_country") @ColumnInfo(name = DatabaseAnnotation.ORIGIN_COUNTRY) val originCountry: List<String>,
    @SerializedName("genre_ids") @ColumnInfo(name = DatabaseAnnotation.GENRE_IDS) val genreIds: List<Int>,
    @SerializedName("original_language") @ColumnInfo(name = DatabaseAnnotation.ORIGINAL_LANGUAGE) val originalLanguage: String,
    @SerializedName("vote_count") @ColumnInfo(name = DatabaseAnnotation.VOTE_COUNT) val voteCount: Int,
    @SerializedName("name") @ColumnInfo(name = DatabaseAnnotation.NAME) val name: String,
    @SerializedName("original_name") @ColumnInfo(name = DatabaseAnnotation.ORIGINAL_NAME) val originalName: String
) : Parcelable
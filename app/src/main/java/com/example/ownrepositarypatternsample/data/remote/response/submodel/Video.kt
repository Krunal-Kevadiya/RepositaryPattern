package com.example.ownrepositarypatternsample.data.remote.response.submodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String,
    @SerializedName("key") val key: String?,
    @SerializedName("size") val size: Int,
    @SerializedName("type") val type: String
) : Parcelable

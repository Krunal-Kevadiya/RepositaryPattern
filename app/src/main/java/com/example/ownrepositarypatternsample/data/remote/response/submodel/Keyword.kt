package com.example.ownrepositarypatternsample.data.remote.response.submodel

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Keyword(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) : Parcelable

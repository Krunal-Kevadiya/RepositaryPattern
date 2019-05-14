package com.example.ownrepositarypatternsample.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersonDetail(
    @SerializedName("birthday") val birthday: String,
    @SerializedName("known_for_department") val knownFoDepartment: String,
    @SerializedName("place_of_birth") val placeOfBirth: String,
    @SerializedName("also_known_as") val alsoKnownAs: List<String>,
    @SerializedName("biography") val bioGraphy: String
) : Parcelable
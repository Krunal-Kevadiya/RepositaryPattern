package com.example.ownrepositarypatternsample.data.remote.response

import android.os.Parcelable
import com.example.ownrepositarypatternsample.base.repository.NetworkResponseModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PersonDetail(
    val birthday: String,
    val known_for_department: String,
    val place_of_birth: String,
    val also_known_as: List<String>,
    val biography: String
) : Parcelable, NetworkResponseModel
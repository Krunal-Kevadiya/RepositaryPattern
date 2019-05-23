package com.example.ownrepositarypatternsample.data.remote.pojo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginPojo(
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var password: String? = null
): Parcelable
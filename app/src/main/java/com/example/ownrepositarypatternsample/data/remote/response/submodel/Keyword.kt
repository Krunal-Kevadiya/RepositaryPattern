package com.example.ownrepositarypatternsample.data.remote.response.submodel

import android.os.Parcel
import android.os.Parcelable

data class Keyword(val id: Int,
                   val name: String) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Keyword> = object : Parcelable.Creator<Keyword> {
            override fun createFromParcel(source: Parcel): Keyword =
                Keyword(source)
            override fun newArray(size: Int): Array<Keyword?> = arrayOfNulls(size)
        }
    }
}

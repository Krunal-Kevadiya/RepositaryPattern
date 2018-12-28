package com.example.ownrepositarypatternsample.data.local.converters

import androidx.room.TypeConverter
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class ReviewListConverter {
    @TypeConverter
    fun fromString(value: String): List<Review>? {
        val listType = object : TypeToken<List<Review>>() {}.type
        return Gson().fromJson<List<Review>>(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Review>?): String {
        return Gson().toJson(list)
    }
}
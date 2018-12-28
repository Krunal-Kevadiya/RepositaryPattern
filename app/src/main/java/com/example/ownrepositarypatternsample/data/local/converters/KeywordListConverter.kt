package com.example.ownrepositarypatternsample.data.local.converters

import androidx.room.TypeConverter
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class KeywordListConverter {
    @TypeConverter
    fun fromString(value: String): List<Keyword>? {
        val listType = object : TypeToken<List<Keyword>>() {}.type
        return Gson().fromJson<List<Keyword>>(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Keyword>?): String {
        return Gson().toJson(list)
    }
}
package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.data.local.entity.People
import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<People>,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
)

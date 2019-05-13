package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.base.repository.NetworkResponseModel
import com.example.ownrepositarypatternsample.data.local.entity.Person
import com.google.gson.annotations.SerializedName

data class PeopleResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Person>,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
) : NetworkResponseModel

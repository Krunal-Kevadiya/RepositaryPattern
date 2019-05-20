package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.google.gson.annotations.SerializedName

data class DiscoverMovieResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Movie>,
    @SerializedName("total_results") val totalResults: Int,
    @SerializedName("total_pages") val totalPages: Int
)

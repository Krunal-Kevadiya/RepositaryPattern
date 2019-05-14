package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.google.gson.annotations.SerializedName

class ReviewListResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<Review>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

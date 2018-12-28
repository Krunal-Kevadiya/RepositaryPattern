package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.base.NetworkResponseModel
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review


class ReviewListResponse(
    val id: Int,
    val page: Int,
    val results: List<Review>,
    val total_pages: Int,
    val total_results: Int
) : NetworkResponseModel

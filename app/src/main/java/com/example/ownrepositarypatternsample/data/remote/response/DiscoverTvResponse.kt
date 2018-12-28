package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.base.NetworkResponseModel
import com.example.ownrepositarypatternsample.data.local.entity.Tv

data class DiscoverTvResponse(
    val page: Int,
    val results: List<Tv>,
    val total_results: Int,
    val total_pages: Int
) : NetworkResponseModel
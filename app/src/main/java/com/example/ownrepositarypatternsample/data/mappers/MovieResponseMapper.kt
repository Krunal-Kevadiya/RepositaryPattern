package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.repository.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverMovieResponse
import timber.log.Timber

class MovieResponseMapper: NetworkResponseMapper<DiscoverMovieResponse> {
    var page = 1

    override fun onLoadPage(): Int = page

    override fun onLastPage(response: DiscoverMovieResponse): Boolean {
        page++
        return response.page > response.totalPages
    }
}

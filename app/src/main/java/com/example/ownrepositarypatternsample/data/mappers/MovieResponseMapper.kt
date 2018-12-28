package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverMovieResponse
import timber.log.Timber

class MovieResponseMapper: NetworkResponseMapper<DiscoverMovieResponse> {
    override fun onLastPage(response: DiscoverMovieResponse): Boolean {
        Timber.d("loadPage : ${response.page}/${response.total_pages}")
        return response.page > response.total_pages
    }
}

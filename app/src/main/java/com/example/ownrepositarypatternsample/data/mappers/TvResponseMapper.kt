package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverTvResponse
import timber.log.Timber

class TvResponseMapper: NetworkResponseMapper<DiscoverTvResponse> {
    override fun onLastPage(response: DiscoverTvResponse): Boolean {
        Timber.d("loadPage : ${response.page}/${response.total_pages}")
        return response.page > response.total_pages
    }
}

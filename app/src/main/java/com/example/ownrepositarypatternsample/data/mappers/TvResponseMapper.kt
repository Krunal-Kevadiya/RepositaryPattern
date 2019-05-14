package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.repository.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverTvResponse
import timber.log.Timber

class TvResponseMapper: NetworkResponseMapper<DiscoverTvResponse> {
    override fun onLoadPage(): Int  = 1
    override fun onLastPage(response: DiscoverTvResponse): Boolean {
        Timber.d("loadPage : ${response.page}/${response.totalPages}")
        return response.page > response.totalPages
    }
}

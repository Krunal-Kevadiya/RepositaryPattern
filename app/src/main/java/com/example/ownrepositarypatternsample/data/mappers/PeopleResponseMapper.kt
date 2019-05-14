package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.repository.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.PeopleResponse
import timber.log.Timber

class PeopleResponseMapper: NetworkResponseMapper<PeopleResponse> {
    override fun onLoadPage(): Int  = 1
    override fun onLastPage(response: PeopleResponse): Boolean {
        Timber.d("loadPage : ${response.page}/${response.totalPages}")
        return response.page > response.totalPages
    }
}

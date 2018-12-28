package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.KeywordListResponse

class KeywordResponseMapper: NetworkResponseMapper<KeywordListResponse> {
    override fun onLastPage(response: KeywordListResponse): Boolean {
        return true
    }
}

package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.ReviewListResponse

class ReviewResponseMapper: NetworkResponseMapper<ReviewListResponse> {
    override fun onLastPage(response: ReviewListResponse): Boolean {
        return true
    }
}

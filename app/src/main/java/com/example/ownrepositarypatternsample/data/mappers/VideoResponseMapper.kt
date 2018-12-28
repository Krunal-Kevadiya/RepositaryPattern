package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.VideoListResponse

class VideoResponseMapper: NetworkResponseMapper<VideoListResponse> {
    override fun onLastPage(response: VideoListResponse): Boolean {
        return true
    }
}

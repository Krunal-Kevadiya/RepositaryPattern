package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.repository.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.VideoListResponse

class VideoResponseMapper: NetworkResponseMapper<VideoListResponse> {
    override fun onLoadPage(): Int  = 1
    override fun onLastPage(response: VideoListResponse): Boolean {
        return true
    }
}

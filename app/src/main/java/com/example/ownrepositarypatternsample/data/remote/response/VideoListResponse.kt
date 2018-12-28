package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.base.NetworkResponseModel
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video


data class VideoListResponse(
    val id: Int,
    val results: List<Video>
) : NetworkResponseModel

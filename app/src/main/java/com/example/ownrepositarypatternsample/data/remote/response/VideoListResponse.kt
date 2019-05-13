package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.base.repository.NetworkResponseModel
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.google.gson.annotations.SerializedName

data class VideoListResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<Video>
) : NetworkResponseModel

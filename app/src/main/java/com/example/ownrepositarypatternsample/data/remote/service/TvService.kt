package com.example.ownrepositarypatternsample.data.remote.service

import androidx.lifecycle.LiveData
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.KeywordListResponse
import com.example.ownrepositarypatternsample.data.remote.response.ReviewListResponse
import com.example.ownrepositarypatternsample.data.remote.response.VideoListResponse
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import retrofit2.http.GET
import retrofit2.http.Path

interface TvService {
    @GET("/3/tv/{tv_id}/keywords")
    fun fetchKeywords(@Path("tv_id") id: Int): LiveData<SealedApiResult<KeywordListResponse, ErrorEnvelope>>

    @GET("/3/tv/{tv_id}/videos")
    fun fetchVideos(@Path("tv_id") id: Int): LiveData<SealedApiResult<VideoListResponse, ErrorEnvelope>>

    @GET("/3/tv/{tv_id}/reviews")
    fun fetchReviews(@Path("tv_id") id: Int): LiveData<SealedApiResult<ReviewListResponse, ErrorEnvelope>>
}

package com.example.ownrepositarypatternsample.data.remote.service

import androidx.lifecycle.LiveData
import com.example.ownrepositarypatternsample.data.remote.response.KeywordListResponse
import com.example.ownrepositarypatternsample.data.remote.response.ReviewListResponse
import com.example.ownrepositarypatternsample.data.remote.response.VideoListResponse
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("/3/movie/{movie_id}/keywords")
    fun fetchKeywords(@Path("movie_id") id: Int): LiveData<SealedApiResult<KeywordListResponse>>

    @GET("/3/movie/{movie_id}/videos")
    fun fetchVideos(@Path("movie_id") id: Int): LiveData<SealedApiResult<VideoListResponse>>

    @GET("/3/movie/{movie_id}/reviews")
    fun fetchReviews(@Path("movie_id") id: Int): LiveData<SealedApiResult<ReviewListResponse>>
}

package com.example.ownrepositarypatternsample.data.remote.service

import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.KeywordListResponse
import com.example.ownrepositarypatternsample.data.remote.response.ReviewListResponse
import com.example.ownrepositarypatternsample.data.remote.response.VideoListResponse
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService {
    @GET("/3/movie/{movie_id}/keywords")
    fun fetchKeywords(@Path("movie_id") id: Int):
            Deferred<SealedApiResult<KeywordListResponse, ErrorEnvelope>>

    @GET("/3/movie/{movie_id}/videos")
    fun fetchVideos(@Path("movie_id") id: Int):
            Deferred<SealedApiResult<VideoListResponse, ErrorEnvelope>>

    @GET("/3/movie/{movie_id}/reviews")
    fun fetchReviews(@Path("movie_id") id: Int):
            Deferred<SealedApiResult<ReviewListResponse, ErrorEnvelope>>
}

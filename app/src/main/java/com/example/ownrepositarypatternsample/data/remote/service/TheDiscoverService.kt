package com.example.ownrepositarypatternsample.data.remote.service

import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverMovieResponse
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverTvResponse
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface TheDiscoverService {
    @GET("/3/discover/movie?language=en&sort_by=popularity.desc")
    fun fetchDiscoverMovie(@Query("page") page: Int):
            Deferred<SealedApiResult<DiscoverMovieResponse, ErrorEnvelope>>

    @GET("/3/discover/tv?language=en&sort_by=popularity.desc")
    fun fetchDiscoverTv(@Query("page") page: Int):
            Deferred<SealedApiResult<DiscoverTvResponse, ErrorEnvelope>>
}

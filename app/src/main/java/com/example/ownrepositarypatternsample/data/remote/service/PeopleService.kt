package com.example.ownrepositarypatternsample.data.remote.service

import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.PeopleResponse
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeopleService {
    @GET("/3/person/popular?language=en")
    fun fetchPopularPeople(@Query("page") page: Int):
            Deferred<SealedApiResult<PeopleResponse, ErrorEnvelope>>

    @GET("/3/person/{person_id}")
    fun fetchPersonDetail(@Path("person_id") id: Int):
            Deferred<SealedApiResult<PersonDetail, ErrorEnvelope>>
}

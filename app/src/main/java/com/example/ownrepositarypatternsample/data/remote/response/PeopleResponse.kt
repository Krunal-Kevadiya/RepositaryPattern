package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.base.NetworkResponseModel
import com.example.ownrepositarypatternsample.data.local.entity.Person

data class PeopleResponse(
    val page: Int,
    val results: List<Person>,
    val total_results: Int,
    val total_pages: Int
) : NetworkResponseModel

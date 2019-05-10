package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.base.repository.NetworkResponseModel
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword

data class KeywordListResponse(
    val id: Int,
    val keywords: List<Keyword>
) : NetworkResponseModel

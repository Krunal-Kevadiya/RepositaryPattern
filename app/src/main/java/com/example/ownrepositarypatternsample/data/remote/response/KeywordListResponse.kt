package com.example.ownrepositarypatternsample.data.remote.response

import com.example.ownrepositarypatternsample.base.repository.NetworkResponseModel
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.google.gson.annotations.SerializedName

data class KeywordListResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("keywords") val keywords: List<Keyword>
) : NetworkResponseModel

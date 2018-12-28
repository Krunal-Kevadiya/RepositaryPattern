package com.example.ownrepositarypatternsample.data.remote.pojo

data class ErrorEnvelope(
    val status_code: Int,
    val status_message: String,
    val success: Boolean
)

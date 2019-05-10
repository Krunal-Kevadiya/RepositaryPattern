package com.example.ownrepositarypatternsample.base.repository

interface NetworkResponseMapper<in FROM: NetworkResponseModel> {
    fun onLastPage(response: FROM): Boolean
}

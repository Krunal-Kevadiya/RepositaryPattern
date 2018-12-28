package com.example.ownrepositarypatternsample.base

interface NetworkResponseMapper<in FROM: NetworkResponseModel> {
    fun onLastPage(response: FROM): Boolean
}

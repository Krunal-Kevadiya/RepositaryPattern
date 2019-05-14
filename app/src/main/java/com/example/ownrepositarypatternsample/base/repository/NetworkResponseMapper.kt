package com.example.ownrepositarypatternsample.base.repository

interface NetworkResponseMapper<in FROM: NetworkResponseModel> {
    fun onLoadPage(): Int
    fun onLastPage(response: FROM): Boolean
}

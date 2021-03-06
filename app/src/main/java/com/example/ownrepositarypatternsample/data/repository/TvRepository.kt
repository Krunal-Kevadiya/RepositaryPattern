package com.example.ownrepositarypatternsample.data.repository

import androidx.lifecycle.LiveData
import com.example.ownrepositarypatternsample.base.repository.NetworkBoundRepository
import com.example.ownrepositarypatternsample.base.repository.RepositoryType
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.local.dao.TvDao
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.KeywordListResponse
import com.example.ownrepositarypatternsample.data.remote.response.ReviewListResponse
import com.example.ownrepositarypatternsample.data.remote.response.VideoListResponse
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.example.ownrepositarypatternsample.data.remote.service.TvService
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

class TvRepository constructor(
    val service: TvService,
    val tvDao: TvDao,
    val ioScope: CoroutineScope
) {
    fun loadKeywordList(id: Int): LiveData<ScreenState<List<Keyword>>> {
        return object: NetworkBoundRepository<List<Keyword>, KeywordListResponse>(
            RepositoryType.Cached, ioScope
        ) {
            override suspend fun saveFetchData(items: KeywordListResponse) {
                val tv = tvDao.getTv(id_ = id)
                tv.keywords = items.keywords
                tvDao.updateTv(tv = tv)
            }

            override fun shouldFetch(data: List<Keyword>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun loadFromDb(): List<Keyword>? {
                val movie = tvDao.getTv(id_ = id)
                return movie.keywords
            }

            override fun loadFromNetwork(items: KeywordListResponse): List<Keyword>? {
                return items.keywords
            }

            override fun fetchService(): Deferred<SealedApiResult<KeywordListResponse, ErrorEnvelope>> {
                return service.fetchKeywords(id = id)
            }

            override fun onLastPage(data: KeywordListResponse): Boolean {
                return true
            }
        }.asLiveData()
    }

    fun loadVideoList(id: Int): LiveData<ScreenState<List<Video>>> {
        return object : NetworkBoundRepository<List<Video>, VideoListResponse>(
            RepositoryType.Cached, ioScope
        ) {
            override suspend fun saveFetchData(items: VideoListResponse) {
                val tv = tvDao.getTv(id_ = id)
                tv.videos = items.results
                tvDao.updateTv(tv = tv)
            }

            override fun shouldFetch(data: List<Video>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun loadFromDb(): List<Video>? {
                val movie = tvDao.getTv(id_ = id)
                return movie.videos
            }

            override fun loadFromNetwork(items: VideoListResponse): List<Video>? {
                return items.results
            }

            override fun fetchService(): Deferred<SealedApiResult<VideoListResponse, ErrorEnvelope>> {
                return service.fetchVideos(id = id)
            }

            override fun onLastPage(data: VideoListResponse): Boolean {
                return true
            }
        }.asLiveData()
    }

    fun loadReviewsList(id: Int): LiveData<ScreenState<List<Review>>> {
        return object: NetworkBoundRepository<List<Review>, ReviewListResponse>(
            RepositoryType.Cached, ioScope
        ) {
            override suspend fun saveFetchData(items: ReviewListResponse) {
                val tv = tvDao.getTv(id_ = id)
                tv.reviews = items.results
                tvDao.updateTv(tv = tv)
            }

            override fun shouldFetch(data: List<Review>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun loadFromDb(): List<Review>? {
                val movie = tvDao.getTv(id_ = id)
                return movie.reviews
            }

            override fun loadFromNetwork(items: ReviewListResponse): List<Review>? {
                return items.results
            }

            override fun fetchService(): Deferred<SealedApiResult<ReviewListResponse, ErrorEnvelope>> {
                return service.fetchReviews(id = id)
            }

            override fun onLastPage(data: ReviewListResponse): Boolean {
                return true
            }
        }.asLiveData()
    }
}

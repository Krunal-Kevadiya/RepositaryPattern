package com.example.ownrepositarypatternsample.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.base.repository.NetworkBoundRepository
import com.example.ownrepositarypatternsample.base.repository.Repository
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.repository.RepositoryType
import com.example.ownrepositarypatternsample.data.local.dao.TvDao
import com.example.ownrepositarypatternsample.data.mappers.KeywordResponseMapper
import com.example.ownrepositarypatternsample.data.mappers.ReviewResponseMapper
import com.example.ownrepositarypatternsample.data.mappers.VideoResponseMapper
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.KeywordListResponse
import com.example.ownrepositarypatternsample.data.remote.response.ReviewListResponse
import com.example.ownrepositarypatternsample.data.remote.response.VideoListResponse
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.example.ownrepositarypatternsample.data.remote.service.TvService
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import timber.log.Timber

class TvRepository constructor(
    val service: TvService,
    val tvDao: TvDao
): Repository {

    init {
        Timber.d("Injection TvRepository")
    }

    fun loadKeywordList(id: Int): LiveData<Resource<List<Keyword>>> {
        return object: NetworkBoundRepository<List<Keyword>, KeywordListResponse, KeywordResponseMapper>(RepositoryType.Cached) {
            override fun saveFetchData(items: KeywordListResponse) {
                val tv = tvDao.getTv(id_ = id)
                tv.keywords = items.keywords
                tvDao.updateTv(tv = tv)
            }

            override fun shouldFetch(data: List<Keyword>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Keyword>> {
                val movie = tvDao.getTv(id_ = id)
                val data: MutableLiveData<List<Keyword>> = MutableLiveData()
                data.value = movie.keywords
                return data
            }

            override fun loadFromNetwork(items: KeywordListResponse): LiveData<List<Keyword>> {
                val result: MutableLiveData<List<Keyword>> = MutableLiveData()
                result.postValue(items.keywords)
                return result
            }

            override fun fetchService(): LiveData<SealedApiResult<KeywordListResponse, ErrorEnvelope>> {
                return service.fetchKeywords(id = id)
            }

            override fun mapper(): KeywordResponseMapper {
                return KeywordResponseMapper()
            }
        }.asLiveData()
    }

    fun loadVideoList(id: Int): LiveData<Resource<List<Video>>> {
        return object : NetworkBoundRepository<List<Video>, VideoListResponse, VideoResponseMapper>(RepositoryType.Cached) {
            override fun saveFetchData(items: VideoListResponse) {
                val tv = tvDao.getTv(id_ = id)
                tv.videos = items.results
                tvDao.updateTv(tv = tv)
            }

            override fun shouldFetch(data: List<Video>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Video>> {
                val movie = tvDao.getTv(id_ = id)
                val data: MutableLiveData<List<Video>> = MutableLiveData()
                data.value = movie.videos
                return data
            }

            override fun loadFromNetwork(items: VideoListResponse): LiveData<List<Video>> {
                val result: MutableLiveData<List<Video>> = MutableLiveData()
                result.postValue(items.results)
                return result
            }

            override fun fetchService(): LiveData<SealedApiResult<VideoListResponse, ErrorEnvelope>> {
                return service.fetchVideos(id = id)
            }

            override fun mapper(): VideoResponseMapper {
                return VideoResponseMapper()
            }
        }.asLiveData()
    }

    fun loadReviewsList(id: Int): LiveData<Resource<List<Review>>> {
        return object: NetworkBoundRepository<List<Review>, ReviewListResponse, ReviewResponseMapper>(RepositoryType.Cached) {
            override fun saveFetchData(items: ReviewListResponse) {
                val tv = tvDao.getTv(id_ = id)
                tv.reviews = items.results
                tvDao.updateTv(tv = tv)
            }

            override fun shouldFetch(data: List<Review>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Review>> {
                val movie = tvDao.getTv(id_ = id)
                val data: MutableLiveData<List<Review>> = MutableLiveData()
                data.value = movie.reviews
                return data
            }

            override fun loadFromNetwork(items: ReviewListResponse): LiveData<List<Review>> {
                val result: MutableLiveData<List<Review>> = MutableLiveData()
                result.postValue(items.results)
                return result
            }

            override fun fetchService(): LiveData<SealedApiResult<ReviewListResponse, ErrorEnvelope>> {
                return service.fetchReviews(id = id)
            }

            override fun mapper(): ReviewResponseMapper {
                return ReviewResponseMapper()
            }
        }.asLiveData()
    }
}

package com.example.ownrepositarypatternsample.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.base.repository.NetworkBoundRepository
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.repository.RepositoryType
import com.example.ownrepositarypatternsample.data.local.dao.MovieDao
import com.example.ownrepositarypatternsample.data.local.dao.TvDao
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverMovieResponse
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverTvResponse
import com.example.ownrepositarypatternsample.data.remote.service.TheDiscoverService
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import kotlinx.coroutines.Deferred

class DiscoverRepository constructor(
    val discoverService: TheDiscoverService,
    val movieDao: MovieDao,
    val tvDao: TvDao
) {

    private var moviePageNumber: Int = 1

    suspend fun loadMovies(): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundRepository<List<Movie>, DiscoverMovieResponse>(RepositoryType.Database, true) {
            override suspend fun saveFetchData(items: DiscoverMovieResponse) {
                for(item in items.results) {
                    item.page = moviePageNumber
                }
                movieDao.insertMovieList(movies = items.results)
            }

            override suspend fun shouldFetch(data: List<Movie>?): Boolean {
                return data == null || data.isEmpty()
            }


            override suspend fun loadFromDb(): List<Movie> {
                return movieDao.getMovieList(moviePageNumber)
            }

            override suspend fun loadFromNetwork(items: DiscoverMovieResponse): List<Movie> {
                return items.results
            }

            override suspend fun fetchService(): SealedApiResult<DiscoverMovieResponse, ErrorEnvelope> {
                return discoverService.fetchDiscoverMovie(page = moviePageNumber).await()
            }

            override suspend fun onLastPage(data: DiscoverMovieResponse): Boolean {
                moviePageNumber++
                return data.page > data.totalPages
            }
        }.asLiveData()
    }


    private var tvPageNumber: Int = 1

    suspend fun loadTvs(): LiveData<Resource<List<Tv>>> {
        return object : NetworkBoundRepository<List<Tv>, DiscoverTvResponse>(RepositoryType.Cached, true) {
            override suspend fun saveFetchData(items: DiscoverTvResponse) {
                for(item in items.results) {
                    item.page = tvPageNumber
                }
                tvDao.insertTv(tvs = items.results)
            }

            override suspend fun shouldFetch(data: List<Tv>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun loadFromDb(): LiveData<List<Tv>> {
                return tvDao.getTvList(page_ = tvPageNumber)
            }

            override suspend fun loadFromNetwork(items: DiscoverTvResponse): LiveData<List<Tv>> {
                val result: MutableLiveData<List<Tv>> = MutableLiveData()
                result.postValue(items.results)
                return result
            }

            override suspend fun fetchService(): Deferred<SealedApiResult<DiscoverTvResponse, ErrorEnvelope>> {
                return discoverService.fetchDiscoverTv(page = tvPageNumber)
            }

            override suspend fun onLastPage(data: DiscoverTvResponse): Boolean {
                return data.page > data.totalPages
            }
        }.asLiveData()
    }
}

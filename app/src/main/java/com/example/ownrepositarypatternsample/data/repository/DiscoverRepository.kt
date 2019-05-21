package com.example.ownrepositarypatternsample.data.repository

import androidx.lifecycle.LiveData
import com.example.ownrepositarypatternsample.base.repository.NetworkBoundRepository
import com.example.ownrepositarypatternsample.base.repository.RepositoryType
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.local.dao.MovieDao
import com.example.ownrepositarypatternsample.data.local.dao.TvDao
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverMovieResponse
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverTvResponse
import com.example.ownrepositarypatternsample.data.remote.service.TheDiscoverService
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

class DiscoverRepository constructor(
    val discoverService: TheDiscoverService,
    val movieDao: MovieDao,
    val tvDao: TvDao,
    val ioScope: CoroutineScope
) {

    fun loadMovies(page: Int): LiveData<ScreenState<List<Movie>>> {
        return object : NetworkBoundRepository<List<Movie>, DiscoverMovieResponse>(
            RepositoryType.Cached, ioScope, page
        ) {
            override suspend fun saveFetchData(items: DiscoverMovieResponse) {
                for (item in items.results) {
                    item.page = page
                }
                movieDao.insertMovieList(movies = items.results)
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun loadFromDb(): List<Movie>? {
                return movieDao.getMovieList(page)
            }

            override fun loadFromNetwork(items: DiscoverMovieResponse): List<Movie>? {
                return items.results
            }

            override fun fetchService(): Deferred<SealedApiResult<DiscoverMovieResponse, ErrorEnvelope>> {
                return discoverService.fetchDiscoverMovie(page = page)
            }

            override fun onLastPage(data: DiscoverMovieResponse): Boolean {
                return data.page > data.totalPages
            }
        }.asLiveData()
    }

    fun loadTvs(page: Int): LiveData<ScreenState<List<Tv>>> {
        return object : NetworkBoundRepository<List<Tv>, DiscoverTvResponse>(
            RepositoryType.Cached, ioScope, page
        ) {
            override suspend fun saveFetchData(items: DiscoverTvResponse) {
                for (item in items.results) {
                    item.page = page
                }
                tvDao.insertTv(tvs = items.results)
            }

            override fun shouldFetch(data: List<Tv>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun loadFromDb(): List<Tv>? {
                return tvDao.getTvList(page_ = page)
            }

            override fun loadFromNetwork(items: DiscoverTvResponse): List<Tv>? {
                return items.results
            }

            override fun fetchService(): Deferred<SealedApiResult<DiscoverTvResponse, ErrorEnvelope>> {
                return discoverService.fetchDiscoverTv(page = page)
            }

            override fun onLastPage(data: DiscoverTvResponse): Boolean {
                return data.page > data.totalPages
            }
        }.asLiveData()
    }
}

package com.example.ownrepositarypatternsample.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.base.repository.NetworkBoundRepository
import com.example.ownrepositarypatternsample.base.repository.Repository
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.repository.RepositoryType
import com.example.ownrepositarypatternsample.data.local.dao.MovieDao
import com.example.ownrepositarypatternsample.data.local.dao.TvDao
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.data.mappers.MovieResponseMapper
import com.example.ownrepositarypatternsample.data.mappers.TvResponseMapper
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverMovieResponse
import com.example.ownrepositarypatternsample.data.remote.response.DiscoverTvResponse
import com.example.ownrepositarypatternsample.data.remote.service.TheDiscoverService
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import timber.log.Timber

class DiscoverRepository constructor(
    val discoverService: TheDiscoverService,
    val movieDao: MovieDao,
    val tvDao: TvDao
) : Repository {

    init {
        Timber.d("Injection DiscoverRepository")
    }

    fun loadMovies(page: Int): LiveData<Resource<List<Movie>>> {
        return object : NetworkBoundRepository<List<Movie>, DiscoverMovieResponse, MovieResponseMapper>(RepositoryType.Cached) {
            override fun saveFetchData(items: DiscoverMovieResponse) {
                for(item in items.results) {
                    item.page = page
                }
                movieDao.insertMovieList(movies = items.results)
            }

            override fun shouldFetch(data: List<Movie>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Movie>> {
                return movieDao.getMovieList(page_ = page)
            }

            override fun loadFromNetwork(items: DiscoverMovieResponse): LiveData<List<Movie>> {
                val result: MutableLiveData<List<Movie>> = MutableLiveData()
                result.postValue(items.results)
                return result
            }

            override fun fetchService(): LiveData<SealedApiResult<DiscoverMovieResponse, ErrorEnvelope>> {
                return discoverService.fetchDiscoverMovie(page = page)
            }

            override fun mapper(): MovieResponseMapper {
                return MovieResponseMapper()
            }
        }.asLiveData()
    }

    fun loadTvs(page: Int): LiveData<Resource<List<Tv>>> {
        return object : NetworkBoundRepository<List<Tv>, DiscoverTvResponse, TvResponseMapper>(RepositoryType.Cached) {
            override fun saveFetchData(items: DiscoverTvResponse) {
                for(item in items.results) {
                    item.page = page
                }
                tvDao.insertTv(tvs = items.results)
            }

            override fun shouldFetch(data: List<Tv>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Tv>> {
                return tvDao.getTvList(page_ = page)
            }

            override fun loadFromNetwork(items: DiscoverTvResponse): LiveData<List<Tv>> {
                val result: MutableLiveData<List<Tv>> = MutableLiveData()
                result.postValue(items.results)
                return result
            }

            override fun fetchService(): LiveData<SealedApiResult<DiscoverTvResponse, ErrorEnvelope>> {
                return discoverService.fetchDiscoverTv(page = page)
            }

            override fun mapper(): TvResponseMapper {
                return TvResponseMapper()
            }
        }.asLiveData()
    }
}

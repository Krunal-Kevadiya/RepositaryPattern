package com.example.ownrepositarypatternsample.ui.main

import androidx.lifecycle.LiveData
import com.example.ownrepositarypatternsample.base.repository.AbsentLiveData
import com.example.ownrepositarypatternsample.base.BaseViewModel
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.local.entity.People
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.data.repository.DiscoverRepository
import com.example.ownrepositarypatternsample.data.repository.PeopleRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainViewModel(
    private val discoverRepository: DiscoverRepository,
    private val peopleRepository: PeopleRepository
) : BaseViewModel() {
    private val job = SupervisorJob()
    private val ioScope = CoroutineScope(job + Dispatchers.IO)

    private var movieListLiveData: LiveData<Resource<List<Movie>>> = AbsentLiveData.create()
    private var tvListLiveData: LiveData<Resource<List<Tv>>> = AbsentLiveData.create()
    private var peopleLiveData: LiveData<Resource<List<People>>> = AbsentLiveData.create()

    fun getMovieListObservable() = movieListLiveData
    fun getMovieListValues() = getMovieListObservable().value
    fun postMoviePage() {
        movieListLiveData = discoverRepository.loadMovies(ioScope)
    }

    fun getTvListObservable() = tvListLiveData
    fun getTvListValues() = getTvListObservable().value
    fun postTvPage() {
        tvListLiveData = discoverRepository.loadTvs(ioScope)
    }

    fun getPeopleListObservable() = peopleLiveData
    fun getPeopleListValues() = getPeopleListObservable().value
    fun postPeoplePage() {
        peopleLiveData = peopleRepository.loadPeople(ioScope)
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}

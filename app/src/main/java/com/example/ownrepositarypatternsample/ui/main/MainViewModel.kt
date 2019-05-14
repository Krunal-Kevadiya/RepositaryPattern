package com.example.ownrepositarypatternsample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.ownrepositarypatternsample.base.repository.AbsentLiveData
import com.example.ownrepositarypatternsample.base.BaseViewModel
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.local.entity.People
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.data.repository.DiscoverRepository
import com.example.ownrepositarypatternsample.data.repository.PeopleRepository

class MainViewModel(
    private val discoverRepository: DiscoverRepository,
    private val peopleRepository: PeopleRepository
) : BaseViewModel() {
    //private var moviePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private var movieListLiveData: LiveData<Resource<List<Movie>>> = AbsentLiveData.create()

    private var tvPageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val tvListLiveData: LiveData<Resource<List<Tv>>>

    private var peoplePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val peopleLiveData: LiveData<Resource<List<People>>>

    init {
        /*movieListLiveData = Transformations.switchMap(moviePageLiveData) {
            moviePageLiveData.value?.let { discoverRepository.loadMovies(it) } ?: AbsentLiveData.create()
        }*/

        tvListLiveData = Transformations.switchMap(tvPageLiveData) {
            tvPageLiveData.value?.let { discoverRepository.loadTvs(it) } ?: AbsentLiveData.create()
        }

        peopleLiveData = Transformations.switchMap(peoplePageLiveData) {
            peoplePageLiveData.value?.let { peopleRepository.loadPeople(it) } ?: AbsentLiveData.create()
        }
    }

    fun getMovieListObservable() = movieListLiveData
    fun getMovieListValues() = getMovieListObservable().value
    fun postMoviePage() {
        movieListLiveData = discoverRepository.loadMovies()
    }

    fun getTvListObservable() = tvListLiveData
    fun getTvListValues() = getTvListObservable().value
    fun postTvPage() {
        if(tvPageLiveData.value != null) {
            val page: Int = tvPageLiveData.value!! + 1
            tvPageLiveData.postValue(page)
        } else {
            tvPageLiveData.postValue(1)
        }
    }

    fun getPeopleListObservable() = peopleLiveData
    fun getPeopleListValues() = getPeopleListObservable().value
    fun postPeoplePage() {
        if(peoplePageLiveData.value != null) {
            val page: Int = peoplePageLiveData.value!! + 1
            peoplePageLiveData.postValue(page)
        } else {
            peoplePageLiveData.postValue(1)
        }
    }
}

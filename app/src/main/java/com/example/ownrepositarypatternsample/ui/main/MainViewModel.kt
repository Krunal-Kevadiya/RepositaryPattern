package com.example.ownrepositarypatternsample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.ownrepositarypatternsample.base.BaseViewModel
import com.example.ownrepositarypatternsample.base.repository.AbsentLiveData
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.local.entity.People
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.data.repository.DiscoverRepository
import com.example.ownrepositarypatternsample.data.repository.PeopleRepository
import kotlinx.coroutines.cancel

class MainViewModel(
    private val discoverRepository: DiscoverRepository,
    private val peopleRepository: PeopleRepository
) : BaseViewModel() {
    private var moviePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private var movieListLiveData: LiveData<ScreenState<List<Movie>>> = AbsentLiveData.create()

    private var tvPageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val tvListLiveData: LiveData<ScreenState<List<Tv>>>

    private var peoplePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val peopleLiveData: LiveData<ScreenState<List<People>>>

    init {
        movieListLiveData = Transformations.switchMap(moviePageLiveData) {
            moviePageLiveData.value?.let { discoverRepository.loadMovies(it) } ?: AbsentLiveData.create()
        }

        tvListLiveData = Transformations.switchMap(tvPageLiveData) {
            tvPageLiveData.value?.let { discoverRepository.loadTvs(it) } ?: AbsentLiveData.create()
        }

        peopleLiveData = Transformations.switchMap(peoplePageLiveData) {
            peoplePageLiveData.value?.let { peopleRepository.loadPeople(it) } ?: AbsentLiveData.create()
        }
    }

    fun getMovieListObservable() = movieListLiveData
    fun postMoviePage() {
        if(moviePageLiveData.value != null) {
            val page: Int = moviePageLiveData.value!! + 1
            moviePageLiveData.postValue(page)
        } else {
            moviePageLiveData.postValue(1)
        }
    }

    fun getTvListObservable() = tvListLiveData
    fun postTvPage() {
        if(tvPageLiveData.value != null) {
            val page: Int = tvPageLiveData.value!! + 1
            tvPageLiveData.postValue(page)
        } else {
            tvPageLiveData.postValue(1)
        }
    }

    fun getPeopleListObservable() = peopleLiveData
    fun postPeoplePage() {
        if(peoplePageLiveData.value != null) {
            val page: Int = peoplePageLiveData.value!! + 1
            peoplePageLiveData.postValue(page)
        } else {
            peoplePageLiveData.postValue(1)
        }
    }

    override fun onCleared() {
        super.onCleared()
        discoverRepository.ioScope.cancel()
        peopleRepository.ioScope.cancel()
    }
}

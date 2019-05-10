package com.example.ownrepositarypatternsample.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.ownrepositarypatternsample.base.repository.AbsentLiveData
import com.example.ownrepositarypatternsample.base.BaseViewModel
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.local.entity.Movie
import com.example.ownrepositarypatternsample.data.local.entity.Person
import com.example.ownrepositarypatternsample.data.local.entity.Tv
import com.example.ownrepositarypatternsample.data.repository.DiscoverRepository
import com.example.ownrepositarypatternsample.data.repository.PeopleRepository
import timber.log.Timber

class MainViewModel(
    private val discoverRepository: DiscoverRepository,
    private val peopleRepository: PeopleRepository
) : BaseViewModel() {
    private var moviePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val movieListLiveData: LiveData<Resource<List<Movie>>>

    private var tvPageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val tvListLiveData: LiveData<Resource<List<Tv>>>

    private var peoplePageLiveData: MutableLiveData<Int> = MutableLiveData()
    private val peopleLiveData: LiveData<Resource<List<Person>>>

    init {
        Timber.d("injection MainViewModel")

        movieListLiveData = Transformations.switchMap(moviePageLiveData) {
            moviePageLiveData.value?.let {
                discoverRepository.loadMovies(it)
            } ?: AbsentLiveData.create()
        }

        tvListLiveData = Transformations.switchMap(tvPageLiveData) {
            tvPageLiveData.value?.let { discoverRepository.loadTvs(it) } ?: AbsentLiveData.create()
        }

        peopleLiveData = Transformations.switchMap(peoplePageLiveData) {
            peoplePageLiveData.value?.let { peopleRepository.loadPeople(it) } ?: AbsentLiveData.create()
        }
    }

    fun getMovieListObservable() = movieListLiveData
    fun getMovieListValues() = getMovieListObservable().value
    fun postMoviePage(page: Int) = moviePageLiveData.postValue(page)

    fun getTvListObservable() = tvListLiveData
    fun getTvListValues() = getTvListObservable().value
    fun postTvPage(page: Int) = tvPageLiveData.postValue(page)

    fun getPeopleObservable() = peopleLiveData
    fun getPeopleValues() = getPeopleObservable().value
    fun postPeoplePage(page: Int) = peoplePageLiveData.postValue(page)
}

package com.example.ownrepositarypatternsample.ui.movie.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.ownrepositarypatternsample.base.BaseViewModel
import com.example.ownrepositarypatternsample.base.repository.AbsentLiveData
import com.example.ownrepositarypatternsample.base.repository.ScreenState
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Keyword
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Review
import com.example.ownrepositarypatternsample.data.remote.response.submodel.Video
import com.example.ownrepositarypatternsample.data.repository.MovieRepository
import kotlinx.coroutines.cancelChildren

class MovieDetailViewModel(
    private val repository: MovieRepository
) : BaseViewModel() {
    private val keywordIdLiveData: MutableLiveData<Int> = MutableLiveData()
    private val keywordListLiveData: LiveData<ScreenState<List<Keyword>>>

    private val videoIdLiveData: MutableLiveData<Int> = MutableLiveData()
    private val videoListLiveData: LiveData<ScreenState<List<Video>>>

    private val reviewIdLiveData: MutableLiveData<Int> = MutableLiveData()
    private val reviewListLiveData: LiveData<ScreenState<List<Review>>>

    init {
        keywordListLiveData = Transformations.switchMap(keywordIdLiveData) {
            keywordIdLiveData.value?.let { repository.loadKeywordList(it) } ?: AbsentLiveData.create()
        }

        videoListLiveData = Transformations.switchMap(videoIdLiveData) {
            videoIdLiveData.value?.let { repository.loadVideoList(it) } ?: AbsentLiveData.create()
        }

        reviewListLiveData = Transformations.switchMap(reviewIdLiveData) {
            reviewIdLiveData.value?.let { repository.loadReviewsList(it) } ?: AbsentLiveData.create()
        }
    }

    fun getKeywordListObservable() = keywordListLiveData
    fun postKeywordId(id: Int) = keywordIdLiveData.postValue(id)

    fun getVideoListObservable() = videoListLiveData
    fun postVideoId(id: Int) = videoIdLiveData.postValue(id)

    fun getReviewListObservable() = reviewListLiveData
    fun postReviewId(id: Int) = reviewIdLiveData.postValue(id)

    override fun onCleared() {
        super.onCleared()
        repository.ioScope.coroutineContext.cancelChildren()
    }
}

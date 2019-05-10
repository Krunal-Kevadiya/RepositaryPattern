package com.example.ownrepositarypatternsample.base.repository

import androidx.lifecycle.LiveData

class AbsentLiveData <T> : LiveData<T>() {
    init {
        postValue(null)
    }
    companion object {
        fun <T> create() = AbsentLiveData<T>()
    }
}

package com.example.ownrepositarypatternsample.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent

abstract class BaseViewModel : ViewModel() {
    var message: MutableLiveData<String> = MutableLiveData()
}

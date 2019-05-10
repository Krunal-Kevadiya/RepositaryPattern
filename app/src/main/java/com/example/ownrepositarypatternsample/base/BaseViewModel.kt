package com.example.ownrepositarypatternsample.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {
    var message: MutableLiveData<String> = MutableLiveData()
}

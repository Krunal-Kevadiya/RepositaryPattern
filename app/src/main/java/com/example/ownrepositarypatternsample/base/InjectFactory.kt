package com.example.ownrepositarypatternsample.base

import androidx.lifecycle.ViewModelProvider

interface InjectFactory {
    fun initFactory(): ViewModelProvider.Factory?
}
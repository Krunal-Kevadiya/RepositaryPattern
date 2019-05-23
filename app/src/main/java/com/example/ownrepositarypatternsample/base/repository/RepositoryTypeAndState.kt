package com.example.ownrepositarypatternsample.base.repository

sealed class RepositoryType {
    object Network : RepositoryType()
    object Database : RepositoryType()
    object Repository : RepositoryType()
    object Cached : RepositoryType()
}

sealed class ScreenState<out ResultType> {
    sealed class LoadingState<out ResultType> : ScreenState<ResultType>() {
        data class Show<ResultType>(var isInitial: Boolean = false) : ScreenState<ResultType>()
        data class Hide<ResultType>(var isInitial: Boolean = false) : ScreenState<ResultType>()
    }
    sealed class SuccessState<out ResultType> : ScreenState<ResultType>() {
        data class Api<ResultType>(var data: ResultType?, var onLastPage: Boolean = true) : ScreenState<ResultType>()
        data class Validation<ResultType>(var request: ResultType?) : ScreenState<ResultType>()
    }
    sealed class ErrorState<out ResultType> : ScreenState<ResultType>() {
        data class Api<ResultType>(var message: String) : ScreenState<ResultType>()
        data class Validation<ResultType>(var message: Int) : ScreenState<ResultType>()
    }
}

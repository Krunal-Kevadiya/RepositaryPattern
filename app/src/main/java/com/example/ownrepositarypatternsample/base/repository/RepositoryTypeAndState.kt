package com.example.ownrepositarypatternsample.base.repository

sealed class RepositoryType {
    object Network : RepositoryType()
    object Database : RepositoryType()
    object Repository : RepositoryType()
    object Cached : RepositoryType()
}

sealed class ScreenState<out ResultType> {
    sealed class LoadingState<out ResultType> : ScreenState<ResultType>() {
        class ShowInitial<ResultType> : ScreenState<ResultType>()
        class HideInitial<ResultType> : ScreenState<ResultType>()
        class ShowOnDemand<ResultType> : ScreenState<ResultType>()
        class HideOnDemand<ResultType> : ScreenState<ResultType>()
    }
    sealed class SuccessState<out ResultType> : ScreenState<ResultType>() {
        data class Api<ResultType>(var data: ResultType?, var onLastPage: Boolean = true) : ScreenState<ResultType>()
        data class Validation<ResultType>(var request: ResultType?) : ScreenState<ResultType>()
    }
    sealed class ErrorState<out ResultType> : ScreenState<ResultType>() {
        data class Api<ResultType>(var message: String) : ScreenState<ResultType>()
        data class Validation<ResultType>(var message: String) : ScreenState<ResultType>()
    }
}

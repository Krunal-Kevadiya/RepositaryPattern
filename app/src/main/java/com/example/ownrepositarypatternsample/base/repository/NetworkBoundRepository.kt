package com.example.ownrepositarypatternsample.base.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.Status
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import kotlinx.coroutines.*

abstract class NetworkBoundRepository<ResultType, ResponseType> constructor(
    private val repositoryType: RepositoryType,
    private val ioScope: CoroutineScope,
    private val isPaginationRepo: Boolean = false
) {
    companion object {
        const val MSG_ERROR_UNKNOWN = "Error Unknown"
    }

    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    private fun MediatorLiveData<Resource<ResultType>>.addSources(
        status: Status,
        liveData: LiveData<ResultType>? = null,
        onLastPage: Boolean = false,
        msgError: String? = null
    ) {
        when (status) {
            Status.LOADING -> {
                this.postValue(Resource.loading())
            }
            Status.SUCCESS -> {
                liveData?.let { list ->
                    this.addSource<ResultType>(list) {
                        it?.let { newData ->
                            this.postValue(Resource.success(newData, onLastPage))
                        }
                    }
                }
            }
            Status.ERROR -> {
                liveData?.let { list ->
                    this.addSource<ResultType>(list) {
                        it?.let { newData ->
                            this.postValue(Resource.error(msgError ?: MSG_ERROR_UNKNOWN, newData, onLastPage))
                        }
                    }
                }
            }
        }
    }

    fun asLiveData(): LiveData<Resource<ResultType>> {
        initLiveData()
        return result
    }

    private fun ResultType?.toLiveData(): LiveData<ResultType> {
        val liveData: MutableLiveData<ResultType> = MutableLiveData()
        this?.run {
            liveData.postValue(this)
        }
        return liveData
    }

    private fun SealedApiResult<ResponseType, ErrorEnvelope>?.toLiveData(): LiveData<SealedApiResult<ResponseType, ErrorEnvelope>> {
        val liveData: MutableLiveData<SealedApiResult<ResponseType, ErrorEnvelope>> = MutableLiveData()
        this?.run {
            liveData.postValue(this)
        }
        return liveData
    }

    open suspend fun saveFetchData(items: ResponseType) {}
    open suspend fun loadFromDb(): ResultType? {
        return null
    }

    open fun onLastPage(data: ResponseType): Boolean {
        return false
    }

    open fun shouldFetch(data: ResultType?): Boolean {
        return false
    }

    open fun loadFromNetwork(items: ResponseType): ResultType? {
        return null
    }

    open fun fetchService(): Deferred<SealedApiResult<ResponseType, ErrorEnvelope>>? {
        return null
    }

    private fun initLiveData() = when (repositoryType) {
        RepositoryType.Network -> {
            fetchFromNetwork()
        }
        RepositoryType.Database -> {
            fetchFromDatabase()
        }
        RepositoryType.Repository -> {
            fetchFromRepository()
        }
        RepositoryType.Cached -> {
            fetchFromCached()
        }
    }

    private fun fetchFromNetwork() {
        result.addSources(Status.LOADING)
        ioScope.launch {
            val apiResponse = fetchService()?.await().toLiveData()
            result.addSource(apiResponse) { response ->
                result.removeSource(apiResponse)
                when (response) {
                    is SealedApiResult.Some.Success2XX.Ok200 -> {
                        response.body?.let { body ->
                            ioScope.launch {
                                saveFetchData(body)
                                result.addSources(Status.SUCCESS, loadFromDb().toLiveData(), onLastPage(body))
                            }
                        }
                    }
                    is SealedApiResult.Some -> {
                        result.addSources(
                            Status.ERROR,
                            onLastPage = !isPaginationRepo,
                            msgError = response.errorBody?.statusMessage
                        )
                    }
                    is SealedApiResult.NetworkError -> {
                        result.addSources(Status.ERROR, onLastPage = !isPaginationRepo, msgError = response.e.message)
                    }
                    else -> {
                        result.addSources(Status.ERROR, onLastPage = !isPaginationRepo, msgError = MSG_ERROR_UNKNOWN)
                    }
                }
            }
        }
    }

    private fun fetchFromDatabase() {
        result.addSources(Status.LOADING)
        ioScope.launch {
            val loadedFromDB = loadFromDb()
            val loadedFromDBLiveData = loadFromDb().toLiveData()
            result.addSource(loadedFromDBLiveData) {
                result.removeSource(loadedFromDBLiveData)
                result.addSources(Status.SUCCESS, loadedFromDBLiveData, loadedFromDB == null)
            }
        }
    }

    private fun fetchFromRepository() {
        result.addSources(Status.LOADING)
        ioScope.launch {
            val loadedFromDB: LiveData<ResultType> = loadFromDb().toLiveData()
            result.addSource(loadedFromDB) { data ->
                result.removeSource(loadedFromDB)
                if (shouldFetch(data)) {
                    fetchFromRepositoryAndCached()
                } else {
                    result.addSources(Status.SUCCESS, loadedFromDB, false)
                }
            }
        }
    }

    private fun fetchFromCached() {
        result.addSources(Status.LOADING)
        ioScope.launch {
            val loadedFromDB = loadFromDb().toLiveData()
            result.addSource(loadedFromDB) {
                result.removeSource(loadedFromDB)
                result.addSources(Status.SUCCESS, loadedFromDB, false)
                fetchFromRepositoryAndCached()
            }
        }
    }

    private fun fetchFromRepositoryAndCached() {
        ioScope.launch {
            val apiResponse = fetchService()?.await().toLiveData()
            result.addSource(apiResponse) { response ->
                result.removeSource(apiResponse)
                when (response) {
                    is SealedApiResult.Some.Success2XX.Ok200 -> {
                        response.body?.let { body ->
                            ioScope.launch {
                                saveFetchData(body)
                                result.addSources(Status.SUCCESS, loadFromDb().toLiveData(), onLastPage(body))
                            }
                        }
                    }
                    is SealedApiResult.Some -> {
                        result.addSources(
                            Status.ERROR,
                            onLastPage = !isPaginationRepo,
                            msgError = response.errorBody?.statusMessage
                        )
                    }
                    is SealedApiResult.NetworkError -> {
                        result.addSources(Status.ERROR, onLastPage = !isPaginationRepo, msgError = response.e.message)
                    }
                    else -> {
                        result.addSources(Status.ERROR, onLastPage = !isPaginationRepo, msgError = MSG_ERROR_UNKNOWN)
                    }
                }
            }
        }
    }
}

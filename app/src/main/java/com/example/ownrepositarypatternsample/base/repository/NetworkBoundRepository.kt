package com.example.ownrepositarypatternsample.base.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import com.kotlinlibrary.utils.ktx.logs
import kotlinx.coroutines.*

abstract class NetworkBoundRepository<ResultType, RequestType> constructor(
    repositoryType: RepositoryType,
    private val ioScope: CoroutineScope,
    private val page: Int = -1
) {
    private val msgErrorUnknown = "Error Unknown"
    private val result = MediatorLiveData<ScreenState<ResultType>>()
    fun asLiveData() = result as LiveData<ScreenState<ResultType>>

    init {
        when (repositoryType) {
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
    }

    private fun fetchFromNetwork() {
        addProgress()
        ioScope.launch {
            val apiResponse = fetchService()!!.await().toLiveData()
            withContext(Dispatchers.Main) {
                result.addSources(apiResponse) { response ->
                    when (response) {
                        is SealedApiResult.Some.Success2XX.Ok200 -> {
                            response.body?.let { body ->
                                removeProgress()
                                addSuccess(loadFromNetwork(body), onLastPage(body))
                            }
                        }
                        is SealedApiResult.Some -> addFailure(response.errorBody?.statusMessage)
                        is SealedApiResult.NetworkError -> addFailure(response.e.message)
                        else -> addFailure()
                    }
                }
            }
        }
    }

    private fun fetchFromDatabase() {
        addProgress()
        ioScope.launch {
            val loadedFromDB = loadFromDb()
            withContext(Dispatchers.Main) {
                result.addSource(loadedFromDB.toLiveData()) { response ->
                    removeProgress()
                    addSuccess(response, loadedFromDB == null)
                }
            }
        }
    }

    private fun fetchFromRepository() {
        addProgress()
        ioScope.launch {
            val loadedFromDB: LiveData<ResultType> = loadFromDb().toLiveData()
            withContext(Dispatchers.Main) {
                result.addSources(loadedFromDB) { response ->
                    if (shouldFetch(response)) {
                        fetchFromServerAndCached()
                    } else {
                        removeProgress()
                        addSuccess(response, false)
                    }
                }
            }
        }
    }

    private fun fetchFromCached() {
        addProgress()
        ioScope.launch {
            val loadedFromDB = loadFromDb().toLiveData()
            logs("loadedFromDB = ${loadedFromDB.value}")
            withContext(Dispatchers.Main) {
                result.addSources(loadedFromDB) { response ->
                    logs("response = $response")
                    addSuccess(response, false)
                    fetchFromServerAndCached()
                }
            }
        }
    }

    private fun fetchFromServerAndCached() {
        ioScope.launch {
            val apiResponse = fetchService()!!.await().toLiveData()
            withContext(Dispatchers.Main) {
                result.addSources(apiResponse) { response ->
                    when (response) {
                        is SealedApiResult.Some.Success2XX.Ok200 -> {
                            response.body?.let { body ->
                                ioScope.launch {
                                    saveFetchData(body)
                                    withContext(Dispatchers.Main) {
                                        removeProgress()
                                        addSuccess(loadFromDb(), onLastPage(body))
                                    }
                                }
                            }
                        }
                        is SealedApiResult.Some -> addFailure(response.errorBody?.statusMessage)
                        is SealedApiResult.NetworkError -> addFailure(response.e.message)
                        else -> addFailure()
                    }
                }
            }
        }
    }

    @WorkerThread
    open suspend fun saveFetchData(items: RequestType) {
    }

    @WorkerThread
    open suspend fun loadFromDb(): ResultType? = null

    @WorkerThread
    open fun onLastPage(data: RequestType): Boolean = false

    @WorkerThread
    open fun shouldFetch(data: ResultType?): Boolean = false

    @WorkerThread
    open fun loadFromNetwork(items: RequestType): ResultType? = null

    @WorkerThread
    open fun fetchService(): Deferred<SealedApiResult<RequestType, ErrorEnvelope>>? = null

    private fun addProgress() {
        result.addSources(ScreenState.LoadingState.Show<ResultType>(page == 1).toLiveData()) { response ->
            result.postValue(response)
        }
    }

    private fun removeProgress() {
        result.addSources(ScreenState.LoadingState.Hide<ResultType>(page == 1).toLiveData()) { response ->
            result.postValue(response)
        }
    }

    private fun addSuccess(data: ResultType?, onLastPage: Boolean) {
        result.addSources(ScreenState.SuccessState.Api(data, onLastPage).toLiveData()) { response ->
            result.postValue(response)
        }
    }

    private fun addFailure(error: String? = null) {
        removeProgress()
        result.addSources(ScreenState.ErrorState.Api<ResultType>(error ?: msgErrorUnknown).toLiveData()) { response ->
            result.postValue(response)
        }
    }

    private fun ScreenState<ResultType>?.toLiveData(): LiveData<ScreenState<ResultType>> {
        val liveData: MutableLiveData<ScreenState<ResultType>> = MutableLiveData()
        liveData.postValue(this)
        return liveData
    }

    private fun ResultType?.toLiveData(): LiveData<ResultType> {
        val liveData: MutableLiveData<ResultType> = MutableLiveData()
        liveData.postValue(this)
        return liveData
    }

    private fun SealedApiResult<RequestType, ErrorEnvelope>?.toLiveData(): LiveData<SealedApiResult<RequestType, ErrorEnvelope>> {
        val liveData: MutableLiveData<SealedApiResult<RequestType, ErrorEnvelope>> = MutableLiveData()
        liveData.postValue(this)
        return liveData
    }

    @MainThread
    private fun <T, S> MediatorLiveData<T>.addSources(source: LiveData<S>, observer: ((S?) -> Unit)? = null) {
        addSource(source) { data ->
            removeSource(source)
            observer?.invoke(data)
        }
    }
}

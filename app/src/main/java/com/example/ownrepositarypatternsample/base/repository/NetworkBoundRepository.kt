package com.example.ownrepositarypatternsample.base.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.kotlinlibrary.retrofitadapter.SealedApiResult

abstract class NetworkBoundRepository<
        ResultType, RequestType : NetworkResponseModel,
        Mapper : NetworkResponseMapper<RequestType>
        > internal constructor(repositoryType: RepositoryType) {
    companion object {
        const val MSG_ERROR_UNKNOWN = "Error Unknown"
    }

    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    init {
        when (repositoryType) {
            RepositoryType.Network -> {
                result.postValue(Resource.loading())
                fetchFromNetwork()
            }
            RepositoryType.Database -> {
                fetchFromDatabase()
            }
            RepositoryType.Repository -> {
                val loadedFromDB = this.loadFromDb()
                result.addSource(loadedFromDB) { data ->
                    result.removeSource(loadedFromDB)
                    if (shouldFetch(data)) {
                        result.postValue(Resource.loading())
                        fetchFromRepositoryAndCached(loadedFromDB)
                    } else {
                        result.addSource<ResultType>(loadedFromDB) { newData ->
                            setValue(Resource.success(newData, false))
                        }
                    }
                }
            }
            RepositoryType.Cached -> {
                val loadedFromDB = this.loadFromDb()
                result.addSource(loadedFromDB) {
                    result.removeSource(loadedFromDB)
                    result.postValue(Resource.loading())
                    result.addSource<ResultType>(loadedFromDB) { newData ->
                        setValue(Resource.success(newData, false))
                    }
                    fetchFromRepositoryAndCached(loadedFromDB)
                }
            }
            else -> {
                setValue(Resource.error("Please select proper repository type", null, false))
            }
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = fetchService()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is SealedApiResult.Some.Success2XX.Ok200 -> {
                    response.body?.let { body ->
                        result.addSource(loadFromNetwork(body)) { newData ->
                            newData?.let {
                                setValue(Resource.success(newData, mapper().onLastPage(body)))
                            }
                        }
                    }
                }
                is SealedApiResult.Some -> {
                    val msgError = response.errorBody?.statusMessage ?: MSG_ERROR_UNKNOWN
                    result.addSource<ResultType>(MutableLiveData()) { newData ->
                        setValue(Resource.error(msgError, newData, true))
                    }
                }
                is SealedApiResult.NetworkError -> {
                    val msgError = response.e.message ?: MSG_ERROR_UNKNOWN
                    result.addSource<ResultType>(MutableLiveData()) { newData ->
                        setValue(Resource.error(msgError, newData, true))
                    }
                }
                else -> {
                    result.addSource<ResultType>(MutableLiveData()) { newData ->
                        setValue(Resource.error(MSG_ERROR_UNKNOWN, newData, true))
                    }
                }
            }
        }
    }

    private fun fetchFromDatabase() {
        val loadedFromDB = this.loadFromDb()
        result.addSource(loadedFromDB) {
            result.removeSource(loadedFromDB)
            result.addSource<ResultType>(loadedFromDB) { newData ->
                setValue(Resource.success(newData, newData != null))
            }
        }
    }

    private fun fetchFromRepositoryAndCached(loadedFromDB: LiveData<ResultType>) {
        val apiResponse = fetchService()
        result.removeSource(loadedFromDB)
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is SealedApiResult.Some.Success2XX.Ok200 -> {
                    response.body?.let { body ->
                        saveFetchData(body)
                        result.addSource(loadFromDb()) { newData ->
                            newData?.let {
                                setValue(Resource.success(newData, mapper().onLastPage(body)))
                            }
                        }
                    }
                }
                is SealedApiResult.Some -> {
                    val msgError = response.errorBody?.statusMessage ?: MSG_ERROR_UNKNOWN
                    result.addSource<ResultType>(loadedFromDB) { newData ->
                        setValue(Resource.error(msgError, newData, true))
                    }
                }
                is SealedApiResult.NetworkError -> {
                    val msgError = response.e.message ?: MSG_ERROR_UNKNOWN
                    result.addSource<ResultType>(loadedFromDB) { newData ->
                        setValue(Resource.error(msgError, newData, true))
                    }
                }
                else -> {
                    result.addSource<ResultType>(loadedFromDB) { newData ->
                        setValue(Resource.error(MSG_ERROR_UNKNOWN, newData, true))
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        result.value = newValue
    }

    fun asLiveData(): LiveData<Resource<ResultType>> = result

    @WorkerThread
    protected abstract fun saveFetchData(items: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun loadFromNetwork(items: RequestType): LiveData<ResultType>

    @MainThread
    protected abstract fun fetchService(): LiveData<SealedApiResult<RequestType, ErrorEnvelope>>

    @MainThread
    protected abstract fun mapper(): Mapper
}

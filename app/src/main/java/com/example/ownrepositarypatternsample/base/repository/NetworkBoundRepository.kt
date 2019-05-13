package com.example.ownrepositarypatternsample.base.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.example.ownrepositarypatternsample.base.Resource
import com.kotlinlibrary.retrofitadapter.SealedApiResult

abstract class NetworkBoundRepository<ResultType, RequestType : NetworkResponseModel, Mapper : NetworkResponseMapper<RequestType>> internal constructor() {
    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    init {
        val loadedFromDB = this.loadFromDb()
        result.addSource(loadedFromDB) { //data ->
            result.removeSource(loadedFromDB)
            /*if (shouldFetch(data)) {
                result.postValue(Resource.loading(null))
                fetchFromNetwork(loadedFromDB)
            } else {
                result.addSource<ResultType>(loadedFromDB) { newData ->
                    setValue(Resource.success(newData, false))
                }
            }*/
            result.postValue(Resource.loading(null))
            result.addSource<ResultType>(loadedFromDB) { newData ->
                setValue(Resource.success(newData, false))
            }
            fetchFromNetwork(loadedFromDB)
        }
    }

    private fun fetchFromNetwork(loadedFromDB: LiveData<ResultType>) {
        val apiResponse = fetchService()
        result.removeSource(loadedFromDB)
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when(response) {
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
                is SealedApiResult.Some -> println(response.errorBody?.statusMessage)
                is SealedApiResult.NetworkError -> println(result.e.toString())
                else -> {
                    onFetchFailed("Error Unknown")
                    response.message?.let {
                        result.addSource<ResultType>(loadedFromDB) { newData ->
                            setValue(Resource.error(it, newData))
                        }
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        result.value = newValue
    }

    fun asLiveData(): LiveData<Resource<ResultType>> {
        return result
    }

    @WorkerThread
    protected abstract fun saveFetchData(items: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun fetchService(): LiveData<SealedApiResult<RequestType>>

    @MainThread
    protected abstract fun mapper(): Mapper

    @MainThread
    protected abstract fun onFetchFailed(message: String?)
}

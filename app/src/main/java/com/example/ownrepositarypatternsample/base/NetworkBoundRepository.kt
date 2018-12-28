package com.example.ownrepositarypatternsample.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.example.ownrepositarypatternsample.data.remote.pojo.ApiResponse
import timber.log.Timber

abstract class NetworkBoundRepository<ResultType, RequestType : NetworkResponseModel, Mapper : NetworkResponseMapper<RequestType>> internal constructor() {

    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    init {
        Timber.d("Injection NetworkBoundRepository")
        val loadedFromDB = this.loadFromDb()
        result.addSource(loadedFromDB) { data ->
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
            response?.let {
                when (response.isSuccessful) {
                    true -> {
                        response.body?.let {
                            saveFetchData(it)
                            val loaded = loadFromDb()
                            result.addSource(loaded) { newData ->
                                newData?.let {
                                    setValue(Resource.success(newData, mapper().onLastPage(response.body)))
                                }
                            }
                        }
                    }
                    false -> {
                        onFetchFailed(response.message)
                        response.message?.let {
                            result.addSource<ResultType>(loadedFromDB) { newData ->
                                setValue(Resource.error(it, newData))
                            }
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
    protected abstract fun fetchService(): LiveData<ApiResponse<RequestType>>

    @MainThread
    protected abstract fun mapper(): Mapper

    @MainThread
    protected abstract fun onFetchFailed(message: String?)
}

package com.example.ownrepositarypatternsample.base.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import kotlinx.coroutines.Deferred

abstract class NetworkBoundRepository<ResultType, RequestType> constructor(
    private val repositoryType: RepositoryType,
    private val isPaginationRepo: Boolean = false
) {
    companion object {
        const val MSG_ERROR_UNKNOWN = "Error Unknown"
    }

    private val result: MediatorLiveData<Resource<ResultType>> = MediatorLiveData()

    private fun setValue(newValue: Resource<ResultType>) {
        result.value = newValue
    }

    fun asLiveData(): LiveData<Resource<ResultType>> = result

    private fun ResultType?.toLiveData(): LiveData<ResultType> {
        val liveData: MutableLiveData<ResultType> = MutableLiveData()
        this?.run {
            liveData.postValue(this)
        }
        return liveData
    }

    suspend fun initLiveData() = when (repositoryType) {
        RepositoryType.Network -> {
            result.postValue(Resource.loading())
            fetchFromNetwork()
        }
        RepositoryType.Database -> {
            fetchFromDatabase()
        }
        RepositoryType.Repository -> {
            val loadedFromDB = loadFromDb().toLiveData()
            result.addSource(loadedFromDB) { data ->
                result.removeSource(loadedFromDB)
                if (this.shouldFetch(data)) {
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
    }

    private suspend fun fetchFromNetwork() {
        when (val apiResult = fetchService()) {
            is SealedApiResult.Some.Success2XX.Ok200 -> {
                apiResult.body?.let { body ->
                    val apiResponse = loadFromNetwork(body).toLiveData()
                    val onLastPage: Boolean = onLastPage(body)
                    result.addSource(apiResponse) { newData ->
                        newData?.let {
                            setValue(Resource.success(newData, onLastPage))
                        }
                    }
                }
            }
            is SealedApiResult.Some -> {
                val msgError = apiResult.errorBody?.statusMessage ?: MSG_ERROR_UNKNOWN
                result.addSource<ResultType>(MutableLiveData()) { newData ->
                    setValue(Resource.error(msgError, newData, true))
                }
            }
            is SealedApiResult.NetworkError -> {
                val msgError = apiResult.e.message ?: MSG_ERROR_UNKNOWN
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

    private suspend fun fetchFromDatabase() {
        val loadedFromDB = loadFromDb().toLiveData()
        result.addSource(loadedFromDB) {
            result.removeSource(loadedFromDB)
            result.addSource<ResultType>(loadedFromDB) { newData ->
                setValue(Resource.success(newData, newData == null))
            }
        }
    }

    private suspend fun fetchFromRepositoryAndCached(loadedFromDB: LiveData<ResultType>) {
        val apiResult = fetchService().await()
        when (apiResult) {
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

        result.removeSource(loadedFromDB)
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)

        }
    }

    open suspend fun saveFetchData(items: RequestType?) {}
    open suspend fun loadFromDb(): ResultType? {
        return null
    }

    open suspend fun onLastPage(data: RequestType?): Boolean {
        return false
    }

    open suspend fun shouldFetch(data: ResultType?): Boolean {
        return false
    }

    open suspend fun loadFromNetwork(items: RequestType?): ResultType? {
        return null
    }

    open suspend fun fetchService(): SealedApiResult<RequestType, ErrorEnvelope>? {
        return null
    }
}

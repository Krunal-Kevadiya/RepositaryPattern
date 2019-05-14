package com.example.ownrepositarypatternsample.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.base.repository.NetworkBoundRepository
import com.example.ownrepositarypatternsample.base.repository.Repository
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.repository.RepositoryType
import com.example.ownrepositarypatternsample.data.local.dao.PeopleDao
import com.example.ownrepositarypatternsample.data.local.entity.People
import com.example.ownrepositarypatternsample.data.mappers.PeopleResponseMapper
import com.example.ownrepositarypatternsample.data.mappers.PersonDetailResponseMapper
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.PeopleResponse
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail
import com.example.ownrepositarypatternsample.data.remote.service.PeopleService
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import timber.log.Timber

class PeopleRepository constructor(
    val peopleService: PeopleService,
    val peopleDao: PeopleDao
) : Repository {

    init {
        Timber.d("Injection PeopleRepository")
    }

    fun loadPeople(page: Int): LiveData<Resource<List<People>>> {
        return object : NetworkBoundRepository<List<People>, PeopleResponse, PeopleResponseMapper>(RepositoryType.Cached) {
            override fun saveFetchData(items: PeopleResponse) {
                for(item in items.results) {
                    item.page = page
                }
                peopleDao.insertPeople(items.results)
            }

            override fun shouldFetch(data: List<People>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<People>> {
                return peopleDao.getPeople(page_ = page)
            }

            override fun loadFromNetwork(items: PeopleResponse): LiveData<List<People>> {
                val result: MutableLiveData<List<People>> = MutableLiveData()
                result.postValue(items.results)
                return result
            }

            override fun fetchService(): LiveData<SealedApiResult<PeopleResponse, ErrorEnvelope>> {
                return peopleService.fetchPopularPeople(page = page)
            }

            override fun mapper(): PeopleResponseMapper {
                return PeopleResponseMapper()
            }
        }.asLiveData()
    }

    fun loadPersonDetail(id: Int): LiveData<Resource<PersonDetail>> {
        return object : NetworkBoundRepository<PersonDetail, PersonDetail, PersonDetailResponseMapper>(RepositoryType.Cached) {
            override fun saveFetchData(items: PersonDetail) {
                val person = peopleDao.getPerson(id_ = id)
                person.personDetail = items
                peopleDao.updatePerson(people = person)
            }

            override fun shouldFetch(data: PersonDetail?): Boolean {
                return data == null || data.bioGraphy.isEmpty()
            }

            override fun loadFromDb(): LiveData<PersonDetail> {
                val person = peopleDao.getPerson(id_ = id)
                val data : MutableLiveData<PersonDetail> = MutableLiveData()
                data.value = person.personDetail
                return data
            }

            override fun loadFromNetwork(items: PersonDetail): LiveData<PersonDetail> {
                val result: MutableLiveData<PersonDetail> = MutableLiveData()
                result.postValue(items)
                return result
            }

            override fun fetchService(): LiveData<SealedApiResult<PersonDetail, ErrorEnvelope>> {
                return peopleService.fetchPersonDetail(id = id)
            }

            override fun mapper(): PersonDetailResponseMapper {
                return PersonDetailResponseMapper()
            }
        }.asLiveData()
    }
}

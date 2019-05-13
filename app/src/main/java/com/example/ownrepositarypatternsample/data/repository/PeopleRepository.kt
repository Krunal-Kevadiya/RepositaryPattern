package com.example.ownrepositarypatternsample.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ownrepositarypatternsample.base.repository.NetworkBoundRepository
import com.example.ownrepositarypatternsample.base.repository.Repository
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.local.dao.PeopleDao
import com.example.ownrepositarypatternsample.data.local.entity.Person
import com.example.ownrepositarypatternsample.data.mappers.PeopleResponseMapper
import com.example.ownrepositarypatternsample.data.mappers.PersonDetailResponseMapper
import com.example.ownrepositarypatternsample.data.remote.pojo.ApiResponse
import com.example.ownrepositarypatternsample.data.remote.response.PeopleResponse
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail
import com.example.ownrepositarypatternsample.data.remote.service.PeopleService
import timber.log.Timber

class PeopleRepository constructor(
    val peopleService: PeopleService,
    val peopleDao: PeopleDao
) : Repository {

    init {
        Timber.d("Injection PeopleRepository")
    }

    fun loadPeople(page: Int): LiveData<Resource<List<Person>>> {
        return object : NetworkBoundRepository<List<Person>, PeopleResponse, PeopleResponseMapper>() {
            override fun saveFetchData(items: PeopleResponse) {
                for(item in items.results) {
                    item.page = page
                }
                peopleDao.insertPeople(items.results)
            }

            override fun shouldFetch(data: List<Person>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Person>> {
                return peopleDao.getPeople(page_ = page)
            }

            override fun fetchService(): LiveData<ApiResponse<PeopleResponse>> {
                return peopleService.fetchPopularPeople(page = page)
            }

            override fun mapper(): PeopleResponseMapper {
                return PeopleResponseMapper()
            }

            override fun onFetchFailed(message: String?) {
                Timber.d("onFetchFailed : $message")
            }
        }.asLiveData()
    }

    fun loadPersonDetail(id: Int): LiveData<Resource<PersonDetail>> {
        return object : NetworkBoundRepository<PersonDetail, PersonDetail, PersonDetailResponseMapper>() {
            override fun saveFetchData(items: PersonDetail) {
                val person = peopleDao.getPerson(id_ = id)
                person.personDetail = items
                peopleDao.updatePerson(person = person)
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

            override fun fetchService(): LiveData<ApiResponse<PersonDetail>> {
                return peopleService.fetchPersonDetail(id = id)
            }

            override fun mapper(): PersonDetailResponseMapper {
                return PersonDetailResponseMapper()
            }

            override fun onFetchFailed(message: String?) {
                Timber.d("onFetchFailed : $message")
            }
        }.asLiveData()
    }
}

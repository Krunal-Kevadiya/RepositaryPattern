package com.example.ownrepositarypatternsample.data.repository

import androidx.lifecycle.LiveData
import com.example.ownrepositarypatternsample.base.repository.NetworkBoundRepository
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.base.repository.RepositoryType
import com.example.ownrepositarypatternsample.data.local.dao.PeopleDao
import com.example.ownrepositarypatternsample.data.local.entity.People
import com.example.ownrepositarypatternsample.data.remote.pojo.ErrorEnvelope
import com.example.ownrepositarypatternsample.data.remote.response.PeopleResponse
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail
import com.example.ownrepositarypatternsample.data.remote.service.PeopleService
import com.kotlinlibrary.retrofitadapter.SealedApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred

class PeopleRepository constructor(
    val peopleService: PeopleService,
    val peopleDao: PeopleDao
) {

    private var peoplePageNumber: Int = 1
    fun loadPeople(ioScope: CoroutineScope): LiveData<Resource<List<People>>> {
        return object : NetworkBoundRepository<List<People>, PeopleResponse>(
            RepositoryType.Cached, ioScope, true
        ) {
            override suspend fun saveFetchData(items: PeopleResponse) {
                for(item in items.results) {
                    item.page = peoplePageNumber
                }
                peopleDao.insertPeople(items.results)
            }

            override fun shouldFetch(data: List<People>?): Boolean {
                return data == null || data.isEmpty()
            }

            override suspend fun loadFromDb(): List<People>? {
                return peopleDao.getPeople(page_ = peoplePageNumber)
            }

            override fun loadFromNetwork(items: PeopleResponse): List<People>? {
                return items.results
            }

            override fun fetchService(): Deferred<SealedApiResult<PeopleResponse, ErrorEnvelope>> {
                return peopleService.fetchPopularPeople(page = peoplePageNumber)
            }

            override fun onLastPage(data: PeopleResponse): Boolean {
                peoplePageNumber++
                return data.page > data.totalPages
            }
        }.asLiveData()
    }

    fun loadPersonDetail(id: Int, ioScope: CoroutineScope): LiveData<Resource<PersonDetail>> {
        return object : NetworkBoundRepository<PersonDetail, PersonDetail>(
            RepositoryType.Cached, ioScope, false
        ) {
            override suspend fun saveFetchData(items: PersonDetail) {
                val person = peopleDao.getPerson(id_ = id)
                person.personDetail = items
                peopleDao.updatePerson(people = person)
            }

            override fun shouldFetch(data: PersonDetail?): Boolean {
                return data == null || data.bioGraphy.isEmpty()
            }

            override suspend fun loadFromDb(): PersonDetail? {
                val person = peopleDao.getPerson(id_ = id)
                return person.personDetail
            }

            override fun loadFromNetwork(items: PersonDetail): PersonDetail? {
                return items
            }

            override fun fetchService(): Deferred<SealedApiResult<PersonDetail, ErrorEnvelope>> {
                return peopleService.fetchPersonDetail(id = id)
            }

            override fun onLastPage(data: PersonDetail): Boolean {
                return true
            }
        }.asLiveData()
    }
}

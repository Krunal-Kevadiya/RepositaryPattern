package com.example.ownrepositarypatternsample.ui.person.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.ownrepositarypatternsample.base.repository.AbsentLiveData
import com.example.ownrepositarypatternsample.base.BaseViewModel
import com.example.ownrepositarypatternsample.base.Resource
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail
import com.example.ownrepositarypatternsample.data.repository.PeopleRepository

class PersonDetailViewModel(
    private val repository: PeopleRepository
) : BaseViewModel() {
    private val personIdLiveData: MutableLiveData<Int> = MutableLiveData()
    private val personLiveData: LiveData<Resource<PersonDetail>>

    init {
        personLiveData = Transformations.switchMap(personIdLiveData) {
            personIdLiveData.value?.let { repository.loadPersonDetail(it) } ?: AbsentLiveData.create()
        }
    }

    fun getPersonObservable() = personLiveData
    fun postPersonId(id: Int) = personIdLiveData.postValue(id)
}

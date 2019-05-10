package com.example.ownrepositarypatternsample.data.mappers

import com.example.ownrepositarypatternsample.base.repository.NetworkResponseMapper
import com.example.ownrepositarypatternsample.data.remote.response.PersonDetail

class PersonDetailResponseMapper: NetworkResponseMapper<PersonDetail> {
    override fun onLastPage(response: PersonDetail): Boolean {
        return true
    }
}

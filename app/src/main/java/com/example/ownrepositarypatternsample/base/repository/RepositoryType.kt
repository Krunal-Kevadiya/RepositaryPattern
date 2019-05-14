package com.example.ownrepositarypatternsample.base.repository

sealed class RepositoryType {
    object Network : RepositoryType()
    object Database : RepositoryType()
    object Repository : RepositoryType()
    object Cached : RepositoryType()
}
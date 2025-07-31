package com.OrLove.peoplemanager.di

import com.OrLove.peoplemanager.data.repositories.PeopleManagerRepository
import com.OrLove.peoplemanager.data.repositories.PeopleManagerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun providePeopleManagerRepository(repositoryImpl: PeopleManagerRepositoryImpl): PeopleManagerRepository
}
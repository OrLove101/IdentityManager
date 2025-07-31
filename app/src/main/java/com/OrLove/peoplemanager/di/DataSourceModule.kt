package com.OrLove.peoplemanager.di

import android.content.Context
import com.OrLove.peoplemanager.data.local.AppDatabase
import com.OrLove.peoplemanager.data.local.dao.IdentitiesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = AppDatabase.initDatabase(context)

    @Provides
    @Singleton
    fun provideIdentitiesDao(
        db: AppDatabase
    ): IdentitiesDao = db.identitiesDao()
}
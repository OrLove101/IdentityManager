package com.OrLove.peoplemanager.di

import android.content.Context
import com.OrLove.peoplemanager.data.managers.notification.AppNotificationManager
import com.OrLove.peoplemanager.data.managers.notification.AppNotificationManagerImpl
import com.OrLove.peoplemanager.data.managers.recognition.RecognitionManager
import com.OrLove.peoplemanager.data.managers.recognition.RecognitionManagerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideRecognitionManager(
        @ApplicationContext context: Context
    ): RecognitionManager = RecognitionManagerImpl(context = context)

    @Provides
    @Singleton
    fun provideAppNotificationManager(
        @ApplicationContext context: Context
    ): AppNotificationManager = AppNotificationManagerImpl(context = context)
}
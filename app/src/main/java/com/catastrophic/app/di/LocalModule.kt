package com.catastrophic.app.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.catastrophic.app.AssesmentApp
import com.catastrophic.app.data.source.AppDatabase
import com.catastrophic.app.data.source.CatDao
import com.catastrophic.app.data.source.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    fun providesCatDao(database: AppDatabase):CatDao = database.catDao()

    @Provides
    @Singleton
    fun provideDatabase(application: Application):AppDatabase = AppDatabase.getDatabase(application)

    @Provides
    @Singleton
    fun providePreferenceManager():PreferencesManager = PreferencesManager(AssesmentApp.applicationContext())
}
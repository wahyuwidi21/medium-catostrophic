package com.catastrophic.app.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
object GlobalVariable {

    init {
        System.loadLibrary("assesment")
    }

    @Singleton
    fun getApiBaseUrl(): String {
        return getBaseUrl();
    }

    private external fun getBaseUrl(): String
}

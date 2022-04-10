package com.catastrophic.app.di

import com.catastrophic.app.di.NetworkModule.NetworkModule.provideMoshiConverter
import com.catastrophic.app.di.NetworkModule.NetworkModule.provideOkHttpClient
import com.catastrophic.app.data.source.ApiServiceCat
import com.catastrophic.app.di.GlobalVariable.getApiBaseUrl
import com.catastrophic.app.di.NetworkModule.NetworkModule.provideClientUser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object DataSourceModule {

    @Provides
    fun provideClientUser() : ApiServiceCat {
        return provideClientUser(provideOkHttpClient(), getApiBaseUrl() ,provideMoshiConverter())
    }

}
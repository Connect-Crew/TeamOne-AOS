package com.connectcrew.data.di

import com.connectcrew.data.service.AuthApi
import com.connectcrew.data.service.ExternalApi
import com.connectcrew.data.service.ProjectApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object ApiModule {

    @Provides
    @Singleton
    fun provideAuthApi(
        @TeamOneApi retrofit: Retrofit
    ): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideProjectApi(
        @TeamOneApi retrofit: Retrofit
    ): ProjectApi = retrofit.create(ProjectApi::class.java)

    @Provides
    @Singleton
    fun provideExternalApi(
        @NormalApi retrofit: Retrofit
    ): ExternalApi = retrofit.create(ExternalApi::class.java)
}
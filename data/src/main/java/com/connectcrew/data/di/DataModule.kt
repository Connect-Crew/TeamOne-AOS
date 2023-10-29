package com.connectcrew.data.di

import com.connectcrew.data.datasource.sign.local.SignLocalDataSource
import com.connectcrew.data.datasource.sign.remote.SignRemoteDataSource
import com.connectcrew.data.datasource.sign.SignRepositoryImpl
import com.connectcrew.data.datasource.sign.local.SignLocalDataSourceImpl
import com.connectcrew.data.datasource.sign.remote.SignRemoteDataSourceImpl
import com.connectcrew.domain.usecase.sign.SignRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class DataModule {

    @Singleton
    @Binds
    abstract fun bindSignRepository(signRepositoryImpl: SignRepositoryImpl): SignRepository

    @Singleton
    @Binds
    abstract fun bindSignRemoteDataSource(signRemoteDataSourceImpl: SignRemoteDataSourceImpl): SignRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindSignLocalDataSource(sinLocalDataSourceImpl: SignLocalDataSourceImpl): SignLocalDataSource
}
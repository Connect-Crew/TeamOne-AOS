package com.connectcrew.data.di

import com.connectcrew.data.datasource.project.ProjectRepositoryImpl
import com.connectcrew.data.datasource.project.remote.ProjectRemoteDataSource
import com.connectcrew.data.datasource.project.remote.ProjectRemoteDataSourceImpl
import com.connectcrew.data.datasource.sign.SignRepositoryImpl
import com.connectcrew.data.datasource.sign.remote.SignRemoteDataSource
import com.connectcrew.data.datasource.sign.remote.SignRemoteDataSourceImpl
import com.connectcrew.data.datasource.token.TokenRepositoryImpl
import com.connectcrew.data.datasource.token.remote.TokenRemoteDataSource
import com.connectcrew.data.datasource.token.remote.TokenRemoteDataSourceImpl
import com.connectcrew.domain.usecase.project.ProjectRepository
import com.connectcrew.domain.usecase.sign.SignRepository
import com.connectcrew.domain.usecase.token.TokenRepository
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
    abstract fun bindTokenRepository(tokenRepositoryImpl: TokenRepositoryImpl): TokenRepository

    @Singleton
    @Binds
    abstract fun bindTokenRemoteDataSource(tokenRemoteDataSourceImpl: TokenRemoteDataSourceImpl): TokenRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindSignRepository(signRepositoryImpl: SignRepositoryImpl): SignRepository

    @Singleton
    @Binds
    abstract fun bindSignRemoteDataSource(signRemoteDataSourceImpl: SignRemoteDataSourceImpl): SignRemoteDataSource

    @Singleton
    @Binds
    abstract fun bindProjectRepository(projectRepositoryImpl: ProjectRepositoryImpl): ProjectRepository

    @Singleton
    @Binds
    abstract fun bindProjectRemoteDataSource(projectRemoteDataSourceImpl: ProjectRemoteDataSourceImpl): ProjectRemoteDataSource
}
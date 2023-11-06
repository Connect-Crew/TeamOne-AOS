package com.connectcrew.presentation.di

import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import com.connectcrew.presentation.util.delegate.SignViewModelDelegateImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DelegateModule {

    @Singleton
    @Binds
    abstract fun bindSignViewModelDelegate(
        signViewModelDelegateImpl: SignViewModelDelegateImpl
    ): SignViewModelDelegate
}
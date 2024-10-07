package com.connectcrew.presentation.di

import com.connectcrew.presentation.util.firebase.FirebaseUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UtilModule {

    @Singleton
    @Provides
    fun provideFirebaseUtil() = FirebaseUtil
}
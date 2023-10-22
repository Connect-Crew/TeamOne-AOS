package com.connectcrew.data.di

import android.content.Context
import com.connectcrew.data.database.TeamOneDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
internal object DataBaseModule {

    @Singleton
    @Provides
    fun providerTeamOneDatabase(
        @ApplicationContext context: Context
    ): TeamOneDataBase = TeamOneDataBase.buildDatabase(context)
}
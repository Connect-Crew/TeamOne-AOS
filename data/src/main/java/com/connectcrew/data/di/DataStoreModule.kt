package com.connectcrew.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.connectcrew.data.datastore.PreferenceStorageImpl
import com.connectcrew.domain.preference.PreferenceStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DataStoreModule {

    private val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(
        name = PreferenceStorageImpl.PREFS_USER_NAME
    )
    private val Context.teamOneDataStore: DataStore<Preferences> by preferencesDataStore(
        name = PreferenceStorageImpl.PREFS_TEAM_ONE_NAME
    )

    @Singleton
    @Provides
    internal fun providePreferenceStorageModule(
        @ApplicationContext context: Context
    ): PreferenceStorage {
        return PreferenceStorageImpl(context.userDataStore, context.teamOneDataStore)
    }
}
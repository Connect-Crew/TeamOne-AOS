package com.connectcrew.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.connectcrew.domain.preference.PreferenceStorage
import javax.inject.Inject

internal class PreferenceStorageImpl @Inject constructor(
    private val userPreferences: DataStore<Preferences>,
    private val utilPreferences: DataStore<Preferences>,
) : PreferenceStorage {

    companion object {
        const val PREFS_TEAM_ONE_NAME = "PrefTeamOne"
        const val PREFS_USER_NAME = "PrefTeamOneUser"
    }

    object PreferenceKeys {
        //TODO:: 여기에 저장할 키 값을 작성
    }
}
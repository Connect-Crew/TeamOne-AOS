package com.connectcrew.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.connectcrew.data.datastore.PreferenceStorageImpl.PreferenceKeys.PREF_AUTH_TOKEN
import com.connectcrew.data.datastore.PreferenceStorageImpl.PreferenceKeys.PREF_USER
import com.connectcrew.data.model.user.User
import com.connectcrew.data.model.user.asEntity
import com.connectcrew.data.model.user.asExternalModel
import com.connectcrew.data.util.JsonUtil
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
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
        val PREF_AUTH_TOKEN = stringPreferencesKey("pref_auth_token")
        val PREF_USER = stringPreferencesKey("pref_user")
    }

    override suspend fun saveToken(token: String) {
        userPreferences.edit { it[PREF_AUTH_TOKEN] = token }
    }

    override val token: Flow<String?> = userPreferences.data.map { it[PREF_AUTH_TOKEN] }

    override suspend fun saveUser(user: UserEntity) {
        userPreferences.edit {
            it[PREF_USER] = try {
                JsonUtil.getAdapter<User>().toJson(user.asExternalModel())
            } catch (e: Exception) {
                Timber.e("Set User Exception $e")
                ""
            }
        }
    }

    override val user: Flow<UserEntity?> = userPreferences.data.map {
        try {
            JsonUtil.getAdapter<User>().fromJson(it[PREF_USER] ?: "")?.asEntity()
        } catch (e: Exception) {
            Timber.e("Get User Exception $e")
            null
        }
    }
}
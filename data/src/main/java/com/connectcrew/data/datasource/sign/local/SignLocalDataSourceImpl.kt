package com.connectcrew.data.datasource.sign.local

import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import javax.inject.Inject

internal class SignLocalDataSourceImpl @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : SignLocalDataSource {

    override suspend fun saveToken(token: String) {
        preferenceStorage.saveToken(token)
    }

    override suspend fun saveLoginSocialType(socialType: String) {
        preferenceStorage.saveSocialType(socialType)
    }

    override suspend fun saveUser(user: UserEntity) {
        preferenceStorage.saveUser(user)
    }
}
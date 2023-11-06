package com.connectcrew.data.datasource.sign

import com.connectcrew.data.datasource.sign.remote.SignRemoteDataSource
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.sign.SignRepository
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import javax.inject.Inject

internal class SignRepositoryImpl @Inject constructor(
    private val remoteDataSource: SignRemoteDataSource,
    private val preferenceStorage: PreferenceStorage
) : SignRepository {

    override suspend fun signIn(accessToken: String, socialType: String): UserEntity {
        return remoteDataSource.signIn(accessToken, socialType)
            .also { saveUserInfo(it) }
    }

    override suspend fun signUp(
        accessToken: String,
        socialType: String,
        userName: String?,
        nickname: String,
        email: String?,
        profileUrl: String?,
        isAdNotificationAgree: Boolean
    ): UserEntity {
        return remoteDataSource.signUp(accessToken, socialType, userName, nickname, email, profileUrl, isAdNotificationAgree)
            .also { saveUserInfo(it) }
    }

    override suspend fun getGoogleTokenInfo(authCode: String): String {
        return remoteDataSource.getGoogleTokenInfo(authCode)
    }

    private suspend fun saveUserInfo(user: UserEntity) {
        preferenceStorage.saveUser(user)
        preferenceStorage.saveToken(user.accessToken)
    }
}
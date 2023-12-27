package com.connectcrew.data.datasource.sign

import com.connectcrew.data.datasource.sign.remote.SignRemoteDataSource
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.sign.SignRepository
import com.connectcrew.domain.usecase.sign.entity.TokenEntity
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import javax.inject.Inject

internal class SignRepositoryImpl @Inject constructor(
    private val remoteDataSource: SignRemoteDataSource,
    private val preferenceStorage: PreferenceStorage
) : SignRepository {

    override suspend fun signIn(accessToken: String, fcmToken: String?, socialType: String): UserEntity {
        return remoteDataSource
            .signIn(accessToken, fcmToken, socialType)
            .also { (user, token) -> saveUserInfo(user, token) }
            .first
    }

    override suspend fun signUp(
        accessToken: String,
        fcmToken: String?,
        socialType: String,
        userName: String?,
        nickname: String,
        email: String?,
        profileUrl: String?
    ): UserEntity {
        return remoteDataSource
            .signUp(accessToken, fcmToken, socialType, userName, nickname, email, profileUrl)
            .also { (user, token) -> saveUserInfo(user, token) }
            .first
    }

    override suspend fun getGoogleTokenInfo(authCode: String): String {
        return remoteDataSource.getGoogleTokenInfo(authCode)
    }

    private suspend fun saveUserInfo(user: UserEntity, tokenEntity: TokenEntity) {
        preferenceStorage.saveUser(user)
        preferenceStorage.saveUserToken(tokenEntity)
    }
}
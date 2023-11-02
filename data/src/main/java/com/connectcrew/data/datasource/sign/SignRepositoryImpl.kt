package com.connectcrew.data.datasource.sign

import com.connectcrew.data.datasource.sign.local.SignLocalDataSource
import com.connectcrew.data.datasource.sign.remote.SignRemoteDataSource
import com.connectcrew.domain.usecase.sign.SignRepository
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import javax.inject.Inject

internal class SignRepositoryImpl @Inject constructor(
    private val remoteDataSource: SignRemoteDataSource,
    private val localDataSource: SignLocalDataSource
) : SignRepository {

    override suspend fun signIn(accessToken: String, socialType: String): UserEntity {
        return remoteDataSource.signIn(accessToken, socialType)
            .also { saveUserInfo(it, accessToken, socialType) }
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
            .also { saveUserInfo(it, accessToken, socialType) }
    }

    override suspend fun getGoogleTokenInfo(authCode: String): String {
        return remoteDataSource.getGoogleTokenInfo(authCode)
    }

    private suspend fun saveUserInfo(user: UserEntity, accessToken: String, socialType: String) {
        localDataSource.saveUser(user)
        localDataSource.saveToken(accessToken)
        localDataSource.saveLoginSocialType(socialType)
    }
}
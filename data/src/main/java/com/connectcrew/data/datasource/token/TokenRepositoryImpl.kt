package com.connectcrew.data.datasource.token

import com.connectcrew.data.datasource.token.remote.TokenRemoteDataSource
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import com.connectcrew.domain.usecase.token.TokenRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class TokenRepositoryImpl @Inject constructor(
    private val remoteDataSource: TokenRemoteDataSource,
    private val preferenceStorage: PreferenceStorage
) : TokenRepository {

    override suspend fun getTokenUsingUserInfo(userInfo: UserEntity?): String? {
        return remoteDataSource.getRefreshToken(userInfo?.refreshToken)
            ?.also { userInfo?.copy(accessToken = it)?.let { user -> saveUserInfo(user) } }
    }

    override suspend fun getTokenUsingRefreshToken(refreshToken: String?): String? {
        return remoteDataSource.getRefreshToken(refreshToken)
            ?.also { preferenceStorage.user.first()?.let { user -> saveUserInfo(user.copy(accessToken = it)) } }
    }

    private suspend fun saveUserInfo(user: UserEntity) {
        preferenceStorage.saveUser(user)
        preferenceStorage.saveToken(user.accessToken)
    }
}
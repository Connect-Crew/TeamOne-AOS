package com.connectcrew.data.datasource.user

import com.connectcrew.data.datasource.user.remote.UserRemoteDataSource
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import com.connectcrew.domain.usecase.user.UserRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val preferenceStorage: PreferenceStorage
) : UserRepository {

    override suspend fun getUserInfo(accessToken: String?): UserEntity {
        val remoteUserInfo = remoteDataSource.getUserInfo(accessToken)
        val localUserInfo = preferenceStorage.user.first()

        return remoteUserInfo.copy(accessToken = localUserInfo?.accessToken ?: "", refreshToken = localUserInfo?.refreshToken ?: "")
            .also { preferenceStorage.saveUser(it) }
    }
}
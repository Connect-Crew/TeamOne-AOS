package com.connectcrew.data.datasource.user

import com.connectcrew.data.datasource.user.remote.UserRemoteDataSource
import com.connectcrew.domain.preference.PreferenceStorage
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import com.connectcrew.domain.usecase.user.UserRepository
import javax.inject.Inject

internal class UserRepositoryImpl @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val preferenceStorage: PreferenceStorage
) : UserRepository {

    override suspend fun getUserInfo(): UserEntity {
        return remoteDataSource.getUserInfo().also {
            preferenceStorage.saveUser(it)
        }
    }
}
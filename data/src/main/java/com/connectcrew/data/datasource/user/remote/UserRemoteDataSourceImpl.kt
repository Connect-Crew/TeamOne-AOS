package com.connectcrew.data.datasource.user.remote

import com.connectcrew.data.model.user.asEntity
import com.connectcrew.data.service.UserApi
import com.connectcrew.data.util.convertToAccessToken
import com.connectcrew.data.util.converterException
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import javax.inject.Inject

internal class UserRemoteDataSourceImpl @Inject constructor(
    private val userApi: UserApi
) : UserRemoteDataSource {

    override suspend fun getUserInfo(accessToken: String?): UserEntity {
        return try {
            userApi.getUserInfo(accessToken.convertToAccessToken()).asEntity()
        } catch (e: Exception) {
            throw converterException(e)
        }
    }
}
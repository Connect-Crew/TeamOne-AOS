package com.connectcrew.data.datasource.token.remote

import com.connectcrew.data.service.AuthApi
import com.connectcrew.data.util.convertToAccessToken
import javax.inject.Inject

internal class TokenRemoteDataSourceImpl @Inject constructor(
    private val authApi: AuthApi
) : TokenRemoteDataSource {

    override suspend fun getRefreshToken(refreshToken: String?): String? {
        return try {
            authApi.getRefreshToken(refreshToken.convertToAccessToken()).token
        } catch (e: Exception) {
            null
        }
    }
}
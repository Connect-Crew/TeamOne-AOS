package com.connectcrew.data.datasource.token.remote

import com.connectcrew.data.service.AuthApi
import javax.inject.Inject

internal class TokenRemoteDataSourceImpl @Inject constructor(
    private val authApi: AuthApi
) : TokenRemoteDataSource {

    override suspend fun getRefreshToken(refreshToken: String?): String? {
        return try {
            authApi.getRefreshToken("Bearer $refreshToken").token
        } catch (e: Exception) {
            null
        }
    }
}
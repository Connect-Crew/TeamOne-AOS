package com.connectcrew.data.datasource.token.remote

interface TokenRemoteDataSource {

    suspend fun getRefreshToken(refreshToken: String?): String?
}
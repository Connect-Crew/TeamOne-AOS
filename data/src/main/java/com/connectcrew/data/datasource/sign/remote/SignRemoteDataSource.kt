package com.connectcrew.data.datasource.sign.remote

import com.connectcrew.domain.usecase.sign.entity.UserEntity

internal interface SignRemoteDataSource {

    suspend fun signIn(
        accessToken: String,
        fcmToken: String?,
        socialType: String
    ): UserEntity

    suspend fun signUp(
        accessToken: String,
        fcmToken: String?,
        socialType: String,
        username: String?,
        nickname: String,
        email: String?,
        profileUrl: String?
    ): UserEntity

    suspend fun getGoogleTokenInfo(authCode: String): String
}
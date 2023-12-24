package com.connectcrew.data.datasource.sign.remote

import com.connectcrew.domain.usecase.sign.entity.TokenEntity
import com.connectcrew.domain.usecase.sign.entity.UserEntity

internal interface SignRemoteDataSource {

    suspend fun signIn(
        accessToken: String,
        fcmToken: String?,
        socialType: String
    ): Pair<UserEntity, TokenEntity>

    suspend fun signUp(
        accessToken: String,
        fcmToken: String?,
        socialType: String,
        username: String?,
        nickname: String,
        email: String?,
        profileUrl: String?
    ): Pair<UserEntity, TokenEntity>

    suspend fun getGoogleTokenInfo(authCode: String): String
}
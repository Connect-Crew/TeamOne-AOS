package com.connectcrew.domain.usecase.token

import com.connectcrew.domain.usecase.sign.entity.UserEntity

interface TokenRepository {

    suspend fun getTokenUsingUserInfo(userInfo: UserEntity?): String?

    suspend fun getTokenUsingRefreshToken(refreshToken: String?): String?
}
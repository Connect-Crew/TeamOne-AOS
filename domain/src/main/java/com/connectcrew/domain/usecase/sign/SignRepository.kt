package com.connectcrew.domain.usecase.sign

import com.connectcrew.domain.usecase.sign.entity.UserEntity

interface SignRepository {

    suspend fun signIn(accessToken: String, fcmToken: String?, socialType: String): UserEntity

    suspend fun signUp(accessToken: String, fcmToken: String?, socialType: String, userName: String?, nickname: String, email: String?, profileUrl: String?): UserEntity

    suspend fun getGoogleTokenInfo(authCode: String): String
}
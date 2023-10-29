package com.connectcrew.data.datasource.sign.local

import com.connectcrew.domain.usecase.sign.entity.UserEntity

internal interface SignLocalDataSource {

    suspend fun saveToken(token: String)

    suspend fun saveLoginSocialType(socialType: String)

    suspend fun saveUser(user: UserEntity)
}
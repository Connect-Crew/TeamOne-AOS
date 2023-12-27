package com.connectcrew.domain.preference

import com.connectcrew.domain.usecase.sign.entity.TokenEntity
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    suspend fun saveUserToken(tokenEntity: TokenEntity)
    val token: Flow<TokenEntity?>

    suspend fun saveUser(user: UserEntity)
    val user: Flow<UserEntity?>
}
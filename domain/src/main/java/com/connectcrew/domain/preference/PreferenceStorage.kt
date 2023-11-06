package com.connectcrew.domain.preference

import com.connectcrew.domain.usecase.sign.entity.UserEntity
import kotlinx.coroutines.flow.Flow

interface PreferenceStorage {

    suspend fun saveToken(token: String)
    val token : Flow<String?>

    suspend fun saveUser(user: UserEntity)
    val user: Flow<UserEntity?>
}
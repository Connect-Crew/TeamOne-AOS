package com.connectcrew.domain.usecase.user

import com.connectcrew.domain.usecase.sign.entity.UserEntity

interface UserRepository {

    suspend fun getUserInfo(): UserEntity
}
package com.connectcrew.data.datasource.user.remote

import com.connectcrew.domain.usecase.sign.entity.UserEntity

internal interface UserRemoteDataSource {

    suspend fun getUserInfo(): UserEntity
}
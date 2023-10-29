package com.connectcrew.presentation.model.user

import com.connectcrew.domain.usecase.sign.entity.UserEntity

data class User(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String,
    val profile: String?,
    val email: String?,
    val isNewUser: Boolean
)

fun UserEntity.asItem(): User {
    return User(
        accessToken = accessToken,
        refreshToken = refreshToken,
        nickname = nickname,
        profile = profile,
        email = email,
        isNewUser = isNewUser
    )
}
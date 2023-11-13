package com.connectcrew.presentation.model.user

import com.connectcrew.domain.usecase.sign.entity.UserEntity

data class User(
    val id: Int,
    val accessToken: String,
    val refreshToken: String,
    val nickname: String,
    val profile: String?,
    val email: String?,
    val temperature: Float,
    val responseRate: Float,
    val introduction: String?,
    val parts: List<String>,
)

fun UserEntity.asItem(): User {
    return User(
        id = id,
        accessToken = accessToken,
        refreshToken = refreshToken,
        nickname = nickname,
        profile = profile,
        email = email,
        temperature = temperature,
        responseRate = responseRate,
        introduction = introduction,
        parts = parts
    )
}
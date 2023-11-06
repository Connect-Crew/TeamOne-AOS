package com.connectcrew.domain.usecase.sign.entity

import com.connectcrew.domain.usecase.Entity

data class UserEntity(
    val accessToken: String,
    val refreshToken: String,
    val nickname: String,
    val profile: String?,
    val email: String?,
) : Entity
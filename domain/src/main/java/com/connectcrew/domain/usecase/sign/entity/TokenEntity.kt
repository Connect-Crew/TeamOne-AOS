package com.connectcrew.domain.usecase.sign.entity

import com.connectcrew.domain.usecase.Entity

data class TokenEntity(
    val accessToken: String,
    val accessTokenExpired: String,
    val refreshToken: String,
    val refreshTokenExpired: String
) : Entity
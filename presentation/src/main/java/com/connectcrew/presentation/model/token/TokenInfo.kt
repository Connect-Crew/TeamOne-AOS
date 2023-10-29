package com.connectcrew.presentation.model.token

import com.connectcrew.domain.usecase.token.entity.TokenInfoEntity

data class TokenInfo(
    val accessToken: String?,
    val socialType: String?
)

fun TokenInfoEntity.asItem(): TokenInfo {
    return TokenInfo(
        accessToken = accessToken,
        socialType = socialType
    )
}
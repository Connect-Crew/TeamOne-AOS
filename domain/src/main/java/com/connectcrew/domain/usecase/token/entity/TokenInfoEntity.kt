package com.connectcrew.domain.usecase.token.entity

import com.connectcrew.domain.usecase.Entity

data class TokenInfoEntity(
    val accessToken: String?,
    val socialType: String?
): Entity
package com.connectcrew.presentation.model.token

import android.os.Parcelable
import com.connectcrew.domain.usecase.token.entity.TokenInfoEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenInfo(
    val accessToken: String?,
    val socialType: String?
) : Parcelable

fun TokenInfoEntity.asItem(): TokenInfo {
    return TokenInfo(
        accessToken = accessToken,
        socialType = socialType
    )
}
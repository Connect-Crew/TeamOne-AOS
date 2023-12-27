package com.connectcrew.presentation.model.token

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenInfo(
    val accessToken: String?,
    val socialType: String?
) : Parcelable
package com.connectcrew.data.model.google

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class GoogleTokenInfo(
    @Json(name = "access_token")
    val accessToken: String,
    @Json(name = "expires_in")
    val expiresIn: Int,
    @Json(name = "scope")
    val scope: String,
    @Json(name = "token_type")
    val tokenType: String,
    @Json(name = "id_token")
    val idToken: String,
)
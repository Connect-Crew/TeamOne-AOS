package com.connectcrew.data.model.token

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class Token(
    @Json(name="token")
    val token: String,
    @Json(name="exp")
    val expired: String
)
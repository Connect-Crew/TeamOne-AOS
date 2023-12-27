package com.connectcrew.data.model.token

import com.connectcrew.domain.usecase.sign.entity.TokenEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
```
{
"token" : "accessToken",
"exp" : "2023-12-20T00:12:53.906208",
"refresh" : "refreshToken",
"refreshExp" : "2023-12-20T00:12:53.906219"
}
```
 */

@JsonClass(generateAdapter = true)
internal data class Token(
    @Json(name = "token")
    val accessToken: String,
    @Json(name = "exp")
    val accessTokenExpired: String,
    @Json(name = "refresh")
    val refreshToken: String,
    @Json(name = "refreshExp")
    val refreshTokenExpired: String,
)

internal fun Token.asEntity(): TokenEntity {
    return TokenEntity(
        accessToken = accessToken,
        accessTokenExpired = accessTokenExpired,
        refreshToken = refreshToken,
        refreshTokenExpired = refreshTokenExpired
    )
}

internal fun TokenEntity.asExternalModel(): Token {
    return Token(
        accessToken = accessToken,
        accessTokenExpired = accessTokenExpired,
        refreshToken = refreshToken,
        refreshTokenExpired = refreshTokenExpired
    )
}
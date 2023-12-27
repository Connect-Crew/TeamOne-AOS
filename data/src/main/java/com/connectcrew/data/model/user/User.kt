package com.connectcrew.data.model.user

import com.connectcrew.domain.usecase.sign.entity.TokenEntity
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.ZonedDateTime

@JsonClass(generateAdapter = true)
internal data class User(
    // User Info
    @Json(name = "id")
    val id: Int,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "profile")
    val profile: String?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "temperature")
    val temperature: Float,
    @Json(name = "responseRate")
    val responseRate: Float,
    @Json(name = "introduction")
    val introduction: String?,
    @Json(name = "parts")
    val parts: List<String>?,

    // Token Info
    @Json(name = "token")
    val accessToken: String? = null,
    @Json(name = "exp")
    val accessTokenExp: String? = null,
    @Json(name = "refreshToken")
    val refreshToken: String? = null,
    @Json(name = "refreshExp")
    val refreshTokenExp: String? = null
)

internal fun User.asEntity(): UserEntity {
    return UserEntity(
        id = id,
        nickname = nickname,
        profile = profile,
        email = email,
        temperature = temperature,
        responseRate = responseRate,
        introduction = introduction,
        parts = parts ?: emptyList()
    )
}

internal fun User.asTokenEntity(): TokenEntity {
    return TokenEntity(
        accessToken = accessToken ?: "",
        accessTokenExpired = accessTokenExp ?: ZonedDateTime.now().toString(),
        refreshToken = refreshToken ?: "",
        refreshTokenExpired = refreshTokenExp ?: ZonedDateTime.now().toString(),
    )
}

internal fun UserEntity.asExternalModel(): User {
    return User(
        id = id,
        nickname = nickname,
        profile = profile,
        email = email,
        temperature = temperature,
        responseRate = responseRate,
        introduction = introduction,
        parts = parts
    )
}
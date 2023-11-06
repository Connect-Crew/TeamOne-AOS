package com.connectcrew.data.model.user

import com.connectcrew.domain.usecase.sign.entity.UserEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class User(
    @Json(name = "token")
    val accessToken: String,
    @Json(name = "refreshToken")
    val refreshToken: String,
    @Json(name = "nickname")
    val nickname: String,
    @Json(name = "profile")
    val profile: String?,
    @Json(name = "email")
    val email: String?
)

internal fun User.asEntity(): UserEntity {
    return UserEntity(
        accessToken = accessToken,
        refreshToken = refreshToken,
        nickname = nickname,
        profile = profile,
        email = email,
    )
}

internal fun UserEntity.asExternalModel(): User {
    return User(
        accessToken = accessToken,
        refreshToken = refreshToken,
        nickname = nickname,
        profile = profile,
        email = email,
    )
}
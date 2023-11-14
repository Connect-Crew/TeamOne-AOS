package com.connectcrew.data.model.user

import com.connectcrew.domain.usecase.sign.entity.UserEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class User(
    // Token Info
    @Json(name = "token")
    val accessToken: String?,
    @Json(name = "refreshToken")
    val refreshToken: String?,
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
    val parts: List<String>?
)

internal fun User.asEntity(): UserEntity {
    return UserEntity(
        accessToken = accessToken ?: "",
        refreshToken = refreshToken ?: "",
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

internal fun UserEntity.asExternalModel(): User {
    return User(
        accessToken = accessToken,
        refreshToken = refreshToken,
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
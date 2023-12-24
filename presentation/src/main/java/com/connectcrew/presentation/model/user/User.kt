package com.connectcrew.presentation.model.user

import android.os.Parcelable
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val nickname: String,
    val profile: String?,
    val email: String?,
    val temperature: Float,
    val responseRate: Int,
    val introduction: String?,
    val parts: List<String>,
) : Parcelable

fun UserEntity.asItem(): User {
    return User(
        id = id,
        nickname = nickname,
        profile = profile,
        email = email,
        temperature = temperature,
        responseRate = responseRate.toInt(),
        introduction = introduction,
        parts = parts
    )
}
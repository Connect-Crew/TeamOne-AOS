package com.connectcrew.presentation.model.user

import android.os.Parcelable
import com.connectcrew.domain.usecase.project.entity.RepresentProjectEntity
import com.connectcrew.domain.usecase.sign.entity.JobPartEntity
import com.connectcrew.domain.usecase.sign.entity.UserEntity
import com.connectcrew.presentation.model.project.RepresentProject
import com.connectcrew.presentation.model.project.asItem
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
    val parts: List<JobPart>,
    val representProjects: List<RepresentProject>
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
        parts = parts.map(JobPartEntity::asItem),
        representProjects = representProjects.map(RepresentProjectEntity::asItem)
    )
}
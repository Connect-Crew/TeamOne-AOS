package com.connectcrew.domain.usecase.sign.entity

import com.connectcrew.domain.usecase.Entity
import com.connectcrew.domain.usecase.project.entity.RepresentProjectEntity

data class UserEntity(
    val id: Int,
    val nickname: String,
    val profile: String?,
    val email: String?,
    val temperature: Float,
    val responseRate: Float,
    val introduction: String?,
    val parts: List<JobPartEntity>,
    val representProjects: List<RepresentProjectEntity>
) : Entity
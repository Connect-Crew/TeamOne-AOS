package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity
import com.connectcrew.domain.usecase.sign.entity.UserEntity

data class ProjectEnrollmentPartMemberEntity(
    val applyId: Int,
    val user: UserEntity,
    val part: String,
    val message: String,
    val contact: String,
    val state: String,
    val leaderMessage: String?,
    val enrollmentAt: String?
) : Entity
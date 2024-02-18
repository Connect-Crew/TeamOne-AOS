package com.connectcrew.presentation.model.project

import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentPartMemberEntity
import com.connectcrew.presentation.model.user.User
import com.connectcrew.presentation.model.user.asItem

enum class ProjectEnrollmentState {
    WAITING,
    ACCEPT,
    REJECT,
}

data class ProjectEnrollmentPartMember(
    val applyId: Int,
    val user: User,
    val part: String,
    val message: String,
    val contact: String,
    val state: ProjectEnrollmentState,
    val leaderMessage: String?,
    val enrollmentAt: String?,
)

fun ProjectEnrollmentPartMemberEntity.asItem(): ProjectEnrollmentPartMember {
    return ProjectEnrollmentPartMember(
        applyId = applyId,
        user = user.asItem(),
        part = part,
        message = message,
        contact = contact,
        state = ProjectEnrollmentState.entries.find { it.name == state } ?: ProjectEnrollmentState.WAITING,
        leaderMessage = leaderMessage,
        enrollmentAt = enrollmentAt,
    )
}
package com.connectcrew.presentation.model.project

import android.os.Parcelable
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentMemberEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectEnrollmentMember(
    val part: String,
    val partKey: String,
    val category: String,
    val enrollCount: Int,
    val currentCount: Int,
    val maxCount: Int,
    val comment: String?
) : Parcelable {

    val isEnroll: Boolean
        get() = currentCount < maxCount
}


fun ProjectEnrollmentMemberEntity.asItem(): ProjectEnrollmentMember {
    return ProjectEnrollmentMember(
        part = part,
        partKey = partKey,
        category = category,
        enrollCount = enrollCount,
        currentCount = currentCount,
        maxCount = maxCount,
        comment = comment
    )
}
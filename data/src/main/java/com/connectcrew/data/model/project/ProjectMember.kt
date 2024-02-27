package com.connectcrew.data.model.project

import com.connectcrew.data.model.user.User
import com.connectcrew.data.model.user.asEntity
import com.connectcrew.data.model.user.asExternalModel
import com.connectcrew.domain.usecase.project.entity.ProjectMemberEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ProjectMember(
    @Json(name = "profile")
    val profile: User,
    @Json(name = "isLeader")
    val isLeader: Boolean,
    @Json(name = "parts")
    val parts: List<String>
)

internal fun ProjectMember.asEntity(): ProjectMemberEntity = ProjectMemberEntity(
    profile = profile.asEntity(),
    isLeader = isLeader,
    parts = parts
)

internal fun ProjectMemberEntity.asExternalModel(): ProjectMember = ProjectMember(
    profile = profile.asExternalModel(),
    isLeader = isLeader,
    parts = parts
)
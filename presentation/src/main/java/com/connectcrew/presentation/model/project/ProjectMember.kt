package com.connectcrew.presentation.model.project

import android.os.Parcelable
import com.connectcrew.domain.usecase.project.entity.ProjectMemberEntity
import com.connectcrew.presentation.model.user.User
import com.connectcrew.presentation.model.user.asItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectMember(
    val profile: User,
    val isLeader: Boolean,
    val parts: List<String>,
    val canKick: Boolean
) : Parcelable

fun ProjectMemberEntity.asItem(canKick: Boolean): ProjectMember = ProjectMember(
    profile = profile.asItem(),
    isLeader = isLeader,
    parts = parts,
    canKick = canKick
)
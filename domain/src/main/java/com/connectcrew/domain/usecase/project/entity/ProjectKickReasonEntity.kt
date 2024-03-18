package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class ProjectKickReasonEntity(
    val projectId: Long,
    val userId: Int,
    val reasons: List<KickReasonEntity>
) : Entity
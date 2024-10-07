package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class ProjectFeedLikeInfoEntity(
    val projectId: Long,
    val isLike: Boolean,
    val likeCount: Int
) : Entity
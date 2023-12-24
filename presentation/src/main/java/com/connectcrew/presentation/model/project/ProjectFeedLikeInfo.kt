package com.connectcrew.presentation.model.project

import com.connectcrew.domain.usecase.project.entity.ProjectFeedLikeInfoEntity

data class ProjectFeedLikeInfo(
    val projectId: Long,
    val isLike: Boolean,
    val likeCount: Int
)

fun ProjectFeedLikeInfoEntity.asItem(): ProjectFeedLikeInfo {
    return ProjectFeedLikeInfo(
        projectId,
        isLike,
        likeCount
    )
}
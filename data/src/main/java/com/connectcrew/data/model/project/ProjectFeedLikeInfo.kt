package com.connectcrew.data.model.project

import com.connectcrew.domain.usecase.project.entity.ProjectFeedLikeInfoEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ProjectFeedLikeInfo(
    @Json(name = "project")
    val projectId: Int,
    @Json(name = "myFavorite")
    val isLike: Boolean,
    @Json(name = "favorite")
    val likeCount: Int
)

internal fun ProjectFeedLikeInfo.asEntity(): ProjectFeedLikeInfoEntity {
    return ProjectFeedLikeInfoEntity(
        projectId = projectId,
        isLike = isLike,
        likeCount = likeCount
    )
}
package com.connectcrew.data.model.project

import com.connectcrew.domain.usecase.project.entity.RequestRecruitStatusEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class RequestRecruitStatus(
    @Json(name = "part")
    val part: String,
    @Json(name = "comment")
    val comment: String,
    @Json(name = "max")
    val maxCount: Int
)

internal fun RequestRecruitStatusEntity.asExternalModel(): RequestRecruitStatus {
    return RequestRecruitStatus(
        part = part,
        comment = comment,
        maxCount = maxCount
    )
}

@JsonClass(generateAdapter = true)
internal data class ProjectId(
    @Json(name = "value")
    val projectId: Long,
)
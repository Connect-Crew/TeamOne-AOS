package com.connectcrew.data.model.project

import com.connectcrew.domain.usecase.project.entity.RepresentProjectEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class RepresentProject(
    @Json(name = "id")
    val id: Int,
    @Json(name = "thumbnail")
    val thumbnailUrl: String?
)

internal fun RepresentProject.asEntity(): RepresentProjectEntity = RepresentProjectEntity(
    id = id,
    thumbnailUrl = thumbnailUrl
)

internal fun RepresentProjectEntity.asExternalModel(): RepresentProject = RepresentProject(
    id = id,
    thumbnailUrl = thumbnailUrl
)
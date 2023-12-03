package com.connectcrew.data.model.project

import com.connectcrew.domain.usecase.project.entity.ProjectInfoContainerEntity
import com.connectcrew.domain.usecase.project.entity.ProjectInfoEntity
import com.connectcrew.domain.usecase.project.entity.ProjectJobInfoEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal data class ProjectInfoContainer(
    @Json(name = "region")
    val regions: List<ProjectInfo>,
    @Json(name = "job")
    val jobs: List<ProjectJobInfo>,
    @Json(name = "category")
    val category: List<ProjectInfo>,
    @Json(name = "skill")
    val skills: List<String>,
)

@JsonClass(generateAdapter = true)
internal data class ProjectJobInfo(
    @Json(name = "key")
    val key: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "value")
    val value: List<ProjectInfo>
)

@JsonClass(generateAdapter = true)
internal data class ProjectInfo(
    @Json(name = "key")
    val key: String,
    @Json(name = "name")
    val name: String
)

internal fun ProjectInfoContainer.asEntity(): ProjectInfoContainerEntity {
    return ProjectInfoContainerEntity(
        regions = regions.map(ProjectInfo::asEntity),
        jobs = jobs.map(ProjectJobInfo::asEntity),
        category = category.map(ProjectInfo::asEntity),
        skills = skills
    )
}

internal fun ProjectJobInfo.asEntity(): ProjectJobInfoEntity {
    return ProjectJobInfoEntity(
        key = key,
        name = name,
        value = value.map(ProjectInfo::asEntity)
    )
}

internal fun ProjectInfo.asEntity(): ProjectInfoEntity {
    return ProjectInfoEntity(
        key = key,
        name = name
    )
}
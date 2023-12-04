package com.connectcrew.presentation.model.project

import com.connectcrew.domain.usecase.project.entity.ProjectInfoContainerEntity
import com.connectcrew.domain.usecase.project.entity.ProjectInfoEntity
import com.connectcrew.domain.usecase.project.entity.ProjectJobInfoEntity

data class ProjectInfoContainer(
    val regions: List<ProjectInfo>,
    val jobs: List<ProjectJobInfo>,
    val category: List<ProjectInfo>,
    val skills: List<String>,
)

data class ProjectJobInfo(
    val key: String,
    val name: String,
    val value: List<ProjectInfo>
)

data class ProjectInfo(
    val key: String,
    val name: String
)

fun ProjectInfoContainerEntity.asItem(): ProjectInfoContainer {
    return ProjectInfoContainer(
        regions = regions.map(ProjectInfoEntity::asItem),
        jobs = jobs.map(ProjectJobInfoEntity::asItem),
        category = category.map(ProjectInfoEntity::asItem),
        skills = skills
    )
}

fun ProjectJobInfoEntity.asItem(): ProjectJobInfo {
    return ProjectJobInfo(
        key = key,
        name = name,
        value = value.map(ProjectInfoEntity::asItem)
    )
}

fun ProjectInfoEntity.asItem(): ProjectInfo {
    return ProjectInfo(
        key = key,
        name = name
    )
}
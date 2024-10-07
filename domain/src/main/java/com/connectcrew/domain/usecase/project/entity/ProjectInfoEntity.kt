package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class ProjectInfoContainerEntity(
    val regions: List<ProjectInfoEntity>,
    val jobs: List<ProjectJobInfoEntity>,
    val category: List<ProjectInfoEntity>,
    val skills: List<String>,
) : Entity

data class ProjectJobInfoEntity(
    val key: String,
    val name: String,
    val value: List<ProjectInfoEntity>
) : Entity

data class ProjectInfoEntity(
    val key: String,
    val name: String
) : Entity
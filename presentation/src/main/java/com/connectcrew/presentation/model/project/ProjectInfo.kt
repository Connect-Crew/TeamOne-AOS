package com.connectcrew.presentation.model.project

import android.os.Parcelable
import androidx.annotation.DrawableRes
import com.connectcrew.domain.usecase.project.entity.ProjectInfoContainerEntity
import com.connectcrew.domain.usecase.project.entity.ProjectInfoEntity
import com.connectcrew.domain.usecase.project.entity.ProjectJobInfoEntity
import com.connectcrew.domain.usecase.project.entity.RequestRecruitStatusEntity
import com.connectcrew.presentation.screen.feature.projectwrite.ProjectWriteFieldType
import com.connectcrew.presentation.screen.feature.projectwrite.getFiledIcon
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectInfoContainer(
    val regions: List<ProjectInfo>,
    val jobs: List<ProjectJobInfo>,
    val category: List<ProjectFieldInfo>,
    val skills: List<String>,
) : Parcelable

@Parcelize
data class ProjectJobInfo(
    val key: String,
    val name: String,
    val value: List<ProjectInfo>
) : Parcelable

@Parcelize
data class ProjectJobUiModel(
    val mainJobName: String,
    val key: String,
    val name: String,
    val maxCount: Int = 1,
    val comment: String? = null
) : Parcelable

fun ProjectJobUiModel.asEntity(): RequestRecruitStatusEntity {
    return RequestRecruitStatusEntity(
        part = key,
        comment = comment.toString(),
        maxCount = maxCount
    )
}

@Parcelize
data class ProjectInfo(
    val key: String,
    val name: String,
    val isSelected: Boolean = false
) : Parcelable

@Parcelize
data class ProjectFieldInfo(
    val category: ProjectWriteFieldType,
    @DrawableRes val categoryIcon: Int?,
    val isSelected: Boolean = false
) : Parcelable

fun ProjectInfoContainerEntity.asItem(): ProjectInfoContainer {
    return ProjectInfoContainer(
        regions = regions.map(ProjectInfoEntity::asItem),
        jobs = jobs.map(ProjectJobInfoEntity::asItem),
        category = category.mapNotNull {
            val item = ProjectWriteFieldType.entries.find { type -> type.key == it.key }
            if (item != null) ProjectFieldInfo(category = item, categoryIcon = item.getFiledIcon()) else null
        },
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
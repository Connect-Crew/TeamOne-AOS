package com.connectcrew.presentation.model.project

import android.os.Parcelable
import com.connectcrew.domain.usecase.project.entity.RepresentProjectEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RepresentProject(
    val id: Int,
    val thumbnailUrl: String?
) : Parcelable

fun RepresentProjectEntity.asItem(): RepresentProject = RepresentProject(
    id = id,
    thumbnailUrl = thumbnailUrl
)
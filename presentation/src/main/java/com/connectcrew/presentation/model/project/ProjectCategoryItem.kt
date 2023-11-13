package com.connectcrew.presentation.model.project

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectCategoryItem(
    val category: ProjectCategoryType,
    val isSelected: Boolean = false
) : Parcelable
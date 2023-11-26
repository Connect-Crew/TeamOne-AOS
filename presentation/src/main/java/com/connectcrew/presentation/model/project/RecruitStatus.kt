package com.connectcrew.presentation.model.project

import android.os.Parcelable
import com.connectcrew.domain.usecase.project.entity.RecruitStatusEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecruitStatus(
    val part: String,
    val partKey: String,
    val comment: String,
    val currentCount: Int,
    val maxCount: Int,
    val category: String,
    val isApplied: Boolean
) : Parcelable {

    val isEnroll: Boolean
        get() = currentCount <= maxCount
}

fun RecruitStatusEntity.asItem(): RecruitStatus {
    return RecruitStatus(
        part = part,
        partKey = partKey,
        comment = comment,
        currentCount = currentCount,
        maxCount = maxCount,
        category = category,
        isApplied = isApplied ?: false
    )
}

fun RecruitStatus.asEntity(): RecruitStatusEntity {
    return RecruitStatusEntity(
        part = part,
        partKey = partKey,
        comment = comment,
        currentCount = currentCount,
        maxCount = maxCount,
        category = category,
        isApplied = isApplied
    )
}
package com.connectcrew.presentation.model.project

import android.os.Parcelable
import com.connectcrew.domain.usecase.project.entity.RecruitStatusEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecruitStatus(
    val part: String,
    val comment: String,
    val currentCount: Int,
    val maxCount: Int
) : Parcelable

fun RecruitStatusEntity.asItem(): RecruitStatus {
    return RecruitStatus(
        part = part,
        comment = comment,
        currentCount = currentCount,
        maxCount = maxCount
    )
}

fun RecruitStatus.asEntity(): RecruitStatusEntity {
    return RecruitStatusEntity(
        part = part,
        comment = comment,
        currentCount = currentCount,
        maxCount = maxCount
    )
}
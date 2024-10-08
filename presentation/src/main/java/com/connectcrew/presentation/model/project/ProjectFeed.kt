package com.connectcrew.presentation.model.project

import android.os.Parcelable
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.connectcrew.domain.usecase.project.entity.RecruitStatusEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectFeed(
    val id: Long,
    val title: String,
    val region: String,
    val thumbnail: String?,
    val isOnline: Boolean,
    val careerMin: String,
    val careerMax: String,
    val createdAt: String,
    val state: String,
    val likeCount: Int,
    val category: List<String>,
    val goal: String,
    val isLike: Boolean,
    val recruitStatus: List<RecruitStatus>,
    val totalCurrentCount: Int,
    val totalMaxCount: Int
) : Parcelable {

    val isEnroll
        get() = totalCurrentCount < totalMaxCount
}

fun ProjectFeedEntity.asItem(): ProjectFeed {
    return ProjectFeed(
        id = id,
        title = title,
        region = region,
        thumbnail = thumbnail,
        isOnline = isOnline,
        careerMin = careerMin,
        careerMax = careerMax,
        createdAt = createdAt,
        state = state,
        likeCount = likeCount,
        category = category,
        goal = goal,
        isLike = isLike,
        recruitStatus = recruitStatus.filter { !it.isLeaderPart }.map(RecruitStatusEntity::asItem),
        totalCurrentCount = (recruitStatus.filter { !it.isLeaderPart }.sumOf { it.currentCount }),
        totalMaxCount = (recruitStatus.filter { !it.isLeaderPart }.sumOf { it.maxCount })
    )
}

fun ProjectFeed.asEntity(): ProjectFeedEntity {
    return ProjectFeedEntity(
        id = id,
        title = title,
        region = region,
        thumbnail = thumbnail,
        isOnline = isOnline,
        careerMin = careerMin,
        careerMax = careerMax,
        createdAt = createdAt,
        state = state,
        likeCount = likeCount,
        category = category,
        goal = goal,
        isLike = isLike,
        recruitStatus = recruitStatus.map(RecruitStatus::asEntity)
    )
}
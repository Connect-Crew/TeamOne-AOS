package com.connectcrew.presentation.model.project

import android.os.Parcelable
import com.connectcrew.domain.usecase.project.entity.ProjectFeedDetailEntity
import com.connectcrew.domain.usecase.project.entity.RecruitStatusEntity
import com.connectcrew.presentation.model.user.User
import com.connectcrew.presentation.model.user.asItem
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProjectFeedDetail(
    val id: Long,
    val title: String,
    val bannerImageUrls: List<String>,
    val region: String,
    val isOnline: Boolean,
    val createdAt: String,
    val state: String,
    val careerMin: String,
    val careerMax: String,
    val category: List<String>,
    val goal: String,
    val projectIntroduction: String,
    val likeCount: Int,
    val isLike: Boolean,
    val leader: User,
    val recruitStatus: List<RecruitStatus>,
    val skills: List<String>,
) : Parcelable {

    val totalCurrentCount
        get() = (recruitStatus.filter { !it.isLeaderPart }.sumOf { it.currentCount })
    val totalMaxCount
        get() = (recruitStatus.filter { !it.isLeaderPart }.sumOf { it.maxCount })

    val isEnroll: Boolean
        get() = totalCurrentCount < totalMaxCount

    val isRemoveEnable: Boolean
        get() = totalCurrentCount == 0
}

fun ProjectFeedDetailEntity.asItem(): ProjectFeedDetail {
    return ProjectFeedDetail(
        id = id,
        title = title,
        bannerImageUrls = bannerImageUrls,
        region = region,
        isOnline = isOnline,
        createdAt = createdAt,
        state = state,
        careerMin = careerMin,
        careerMax = careerMax,
        category = category,
        goal = goal,
        projectIntroduction = projectIntroduction,
        likeCount = likeCount,
        isLike = isLike,
        leader = leader.asItem(),
        recruitStatus = recruitStatus.filter { !it.isLeaderPart }.map(RecruitStatusEntity::asItem),
        skills = skills
    )
}

fun ProjectFeedDetail.toSummary(): ProjectFeed {
    return ProjectFeed(
        id = id,
        title = title,
        region = region,
        thumbnail = bannerImageUrls.firstOrNull(),
        isOnline = isOnline,
        careerMin = careerMin,
        careerMax = careerMax,
        createdAt = createdAt,
        state = state,
        likeCount = likeCount,
        category = category,
        goal = goal,
        isLike = isLike,
        recruitStatus = recruitStatus,
        totalCurrentCount = totalCurrentCount,
        totalMaxCount = totalMaxCount
    )
}
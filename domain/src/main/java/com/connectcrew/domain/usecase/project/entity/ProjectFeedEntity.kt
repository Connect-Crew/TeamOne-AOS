package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class ProjectFeedEntity(
    val id: Int,
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
    val recruitStatus: List<RecruitStatusEntity>
) : Entity
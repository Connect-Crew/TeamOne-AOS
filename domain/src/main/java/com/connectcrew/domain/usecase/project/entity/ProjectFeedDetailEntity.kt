package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity
import com.connectcrew.domain.usecase.sign.entity.UserEntity

data class ProjectFeedDetailEntity(
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
    val leader: UserEntity,
    val recruitStatus: List<RecruitStatusEntity>,
    val skills: List<String>
) : Entity
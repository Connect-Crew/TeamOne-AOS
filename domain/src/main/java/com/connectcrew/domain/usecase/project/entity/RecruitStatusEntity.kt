package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class RecruitStatusEntity(
    val part: String,
    val partKey: String,
    val comment: String,
    val currentCount: Int,
    val maxCount: Int,
    val category: String,
    val isLeaderPart: Boolean,
    val isApplied: Boolean?
) : Entity
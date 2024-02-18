package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class ProjectEnrollmentMemberEntity(
    val part: String,
    val partKey: String,
    val category: String,
    val enrollCount: Int,
    val currentCount: Int,
    val maxCount: Int,
    val comment: String?
) : Entity
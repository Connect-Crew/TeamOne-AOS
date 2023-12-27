package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class RequestRecruitStatusEntity(
    val part: String,
    val comment: String,
    val maxCount: Int
) : Entity
package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class RecruitStatusEntity(
    val part: String,
    val comment:String,
    val currentCount: Int,
    val maxCount: Int
) : Entity
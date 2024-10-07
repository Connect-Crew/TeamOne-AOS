package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class RepresentProjectEntity(
    val id: Int,
    val thumbnailUrl: String?
) : Entity
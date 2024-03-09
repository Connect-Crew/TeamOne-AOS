package com.connectcrew.domain.usecase.project.entity

import com.connectcrew.domain.usecase.Entity

data class KickReasonEntity(
    val type: String,
    val reason: String
) : Entity
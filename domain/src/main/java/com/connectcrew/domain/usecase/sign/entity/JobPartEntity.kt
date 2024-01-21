package com.connectcrew.domain.usecase.sign.entity

import com.connectcrew.domain.usecase.Entity

data class JobPartEntity(
    val key: String,
    val value: String,
    val category: String
) : Entity
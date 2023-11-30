package com.connectcrew.domain.usecase.media.entity

import com.connectcrew.domain.usecase.Entity

data class MediaEntity(
    val albumName: String?,
    val uri: String,
    val dateTimeMills: Long
) : Entity
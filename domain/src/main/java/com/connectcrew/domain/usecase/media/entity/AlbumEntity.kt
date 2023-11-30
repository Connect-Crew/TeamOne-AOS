package com.connectcrew.domain.usecase.media.entity

import com.connectcrew.domain.usecase.Entity

data class AlbumEntity(
    val name: String,
    val thumbnailUri: String,
    val mediaSize: Int
) : Entity
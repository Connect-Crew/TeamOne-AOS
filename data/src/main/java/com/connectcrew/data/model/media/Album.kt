package com.connectcrew.data.model.media

import com.connectcrew.domain.usecase.media.entity.AlbumEntity

internal data class Album(
    val name: String,
    val thumbnailUri: String,
    val mediaSize: Int
)

internal fun Album.asEntity(): AlbumEntity {
    return AlbumEntity(
        name = name,
        thumbnailUri = thumbnailUri,
        mediaSize = mediaSize
    )
}

internal fun AlbumEntity.asExternalModel(): Album {
    return Album(
        name = name,
        thumbnailUri = thumbnailUri,
        mediaSize = mediaSize
    )
}
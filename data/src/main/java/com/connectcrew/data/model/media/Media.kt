package com.connectcrew.data.model.media

import com.connectcrew.domain.usecase.media.entity.MediaEntity

internal data class Media(
    val albumName: String?,
    val uri: String,
    val dateTimeMills: Long
)

internal fun Media.asEntity(): MediaEntity {
    return MediaEntity(
        albumName = albumName,
        uri = uri,
        dateTimeMills = dateTimeMills
    )
}

internal fun Media.asExternalModel(): Media {
    return Media(
        albumName = albumName,
        uri = uri,
        dateTimeMills = dateTimeMills
    )
}
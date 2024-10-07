package com.connectcrew.presentation.model.media

import android.os.Parcelable
import com.connectcrew.domain.usecase.media.entity.MediaEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Media(
    val albumName: String?,
    val uri: String,
    val dateTimeMills: Long,
    val selectedPosition: Int? = null // use only presentation-layer
) : Parcelable

fun MediaEntity.asItem(): Media {
    return Media(
        albumName = albumName,
        uri = uri,
        dateTimeMills = dateTimeMills
    )
}

fun Media.asEntity(): MediaEntity {
    return MediaEntity(
        albumName = albumName,
        uri = uri,
        dateTimeMills = dateTimeMills
    )
}
package com.connectcrew.presentation.model.media

import android.os.Parcelable
import com.connectcrew.domain.usecase.media.entity.AlbumEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val name: String,
    val thumbnailUri: String,
    val mediaSize: Int,
    val isSelected: Boolean = false // use only presentation-layer
) : Parcelable

fun AlbumEntity.asItem(): Album {
    return Album(
        name = name,
        thumbnailUri = thumbnailUri,
        mediaSize = mediaSize
    )
}

fun Album.asEntity(): AlbumEntity {
    return AlbumEntity(
        name = name,
        thumbnailUri = thumbnailUri,
        mediaSize = mediaSize
    )
}
package com.connectcrew.data.datasource.media.local

import androidx.paging.PagingData
import com.connectcrew.domain.usecase.media.entity.AlbumEntity
import com.connectcrew.domain.usecase.media.entity.MediaEntity
import kotlinx.coroutines.flow.Flow

internal interface MediaLocalDataSource {

    fun getMediaImages(albumName: String?): Flow<PagingData<MediaEntity>>

    suspend fun getAlbums(albumName: String?): List<AlbumEntity>
}
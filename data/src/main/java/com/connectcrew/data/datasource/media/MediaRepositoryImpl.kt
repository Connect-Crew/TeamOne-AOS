package com.connectcrew.data.datasource.media

import androidx.paging.PagingData
import com.connectcrew.data.datasource.media.local.MediaLocalDataSource
import com.connectcrew.domain.usecase.media.MediaRepository
import com.connectcrew.domain.usecase.media.entity.AlbumEntity
import com.connectcrew.domain.usecase.media.entity.MediaEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class MediaRepositoryImpl @Inject constructor(
    private val localDataSource: MediaLocalDataSource
): MediaRepository {

    override fun getMediaImages(albumName: String?): Flow<PagingData<MediaEntity>> {
        return localDataSource.getMediaImages(albumName)
    }

    override suspend fun getAlbums(albumName: String?): List<AlbumEntity> {
        return localDataSource.getAlbums(albumName)
    }
}
package com.connectcrew.domain.usecase.media

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.UseCase
import com.connectcrew.domain.usecase.media.entity.AlbumEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetMediaAlbumUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<String, List<AlbumEntity>>(ioDispatcher) {

    override suspend fun execute(params: String): List<AlbumEntity> {
        return mediaRepository.getAlbums(params)
    }
}
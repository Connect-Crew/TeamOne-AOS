package com.connectcrew.domain.usecase.media

import androidx.paging.PagingData
import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.media.entity.MediaEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMediaImagesUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<String, PagingData<MediaEntity>>(ioDispatcher) {

    override fun execute(params: String): Flow<PagingData<MediaEntity>> {
        return mediaRepository.getMediaImages(params)
    }
}
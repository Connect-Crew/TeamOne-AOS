package com.connectcrew.domain.usecase.searchhistory

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.UseCase
import com.connectcrew.domain.usecase.searchhistory.entity.SearchHistoryEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SaveSearchHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<SaveSearchHistoryUseCase.Params, Unit>(ioDispatcher) {

    override suspend fun execute(params: Params) {
        searchHistoryRepository.insertSearchHistory(params.searchHistoryEntity)
    }

    data class Params(
        val searchHistoryEntity: SearchHistoryEntity
    )
}
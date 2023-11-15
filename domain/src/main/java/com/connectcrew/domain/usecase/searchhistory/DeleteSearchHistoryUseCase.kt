package com.connectcrew.domain.usecase.searchhistory

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteSearchHistoryUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<DeleteSearchHistoryUseCase.Params, Unit>(ioDispatcher) {

    override suspend fun execute(params: Params) {
        searchHistoryRepository.deleteSearchHistory(
            searchContent = params.content
        )
    }

    data class Params(
        val content: String
    )
}
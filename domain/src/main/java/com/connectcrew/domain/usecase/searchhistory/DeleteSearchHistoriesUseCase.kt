package com.connectcrew.domain.usecase.searchhistory

import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteSearchHistoriesUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : UseCase<Unit, Unit>(ioDispatcher) {

    override suspend fun execute(params: Unit) {
        searchHistoryRepository.deleteSearchHistories()
    }
}
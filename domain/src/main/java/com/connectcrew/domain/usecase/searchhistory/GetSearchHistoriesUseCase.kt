package com.connectcrew.domain.usecase.searchhistory

import androidx.paging.PagingData
import com.connectcrew.domain.di.IoDispatcher
import com.connectcrew.domain.usecase.FlowUseCase
import com.connectcrew.domain.usecase.searchhistory.entity.SearchHistoryEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchHistoriesUseCase @Inject constructor(
    private val searchHistoryRepository: SearchHistoryRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, PagingData<SearchHistoryEntity>>(ioDispatcher) {

    override fun execute(params: Unit): Flow<PagingData<SearchHistoryEntity>> {
        return searchHistoryRepository.getSearchHistories()
    }
}
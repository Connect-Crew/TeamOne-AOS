package com.connectcrew.domain.usecase.searchhistory

import androidx.paging.PagingData
import com.connectcrew.domain.usecase.searchhistory.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    suspend fun insertSearchHistory(searchHistoryEntity: SearchHistoryEntity)

    fun getSearchHistories(): Flow<PagingData<SearchHistoryEntity>>

    suspend fun deleteSearchHistory(searchContent: String)

    suspend fun deleteSearchHistories()
}
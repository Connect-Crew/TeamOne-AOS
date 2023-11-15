package com.connectcrew.data.datasource.searchhistory.local

import androidx.paging.PagingData
import com.connectcrew.data.model.searchhistory.SearchKeyword
import com.connectcrew.domain.usecase.searchhistory.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

internal interface SearchHistoryLocalDataSource {

    suspend fun insertSearchHistory(searchKeyword: SearchKeyword)

    fun getSearchHistories(): Flow<PagingData<SearchHistoryEntity>>

    suspend fun deleteSearchHistory(content: String)

    suspend fun deleteSearchHistories()
}
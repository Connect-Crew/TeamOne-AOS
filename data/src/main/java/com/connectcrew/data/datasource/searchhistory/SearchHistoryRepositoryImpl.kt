package com.connectcrew.data.datasource.searchhistory

import androidx.paging.PagingData
import com.connectcrew.data.datasource.searchhistory.local.SearchHistoryLocalDataSource
import com.connectcrew.data.model.searchhistory.asExternalModel
import com.connectcrew.domain.usecase.searchhistory.SearchHistoryRepository
import com.connectcrew.domain.usecase.searchhistory.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SearchHistoryRepositoryImpl @Inject constructor(
    private val localDataSource: SearchHistoryLocalDataSource
) : SearchHistoryRepository {

    override suspend fun insertSearchHistory(searchHistoryEntity: SearchHistoryEntity) {
        localDataSource.insertSearchHistory(searchHistoryEntity.asExternalModel())
    }

    override fun getSearchHistories(): Flow<PagingData<SearchHistoryEntity>> {
        return localDataSource.getSearchHistories()
    }

    override suspend fun deleteSearchHistory(searchContent: String) {
        localDataSource.deleteSearchHistory(searchContent)
    }

    override suspend fun deleteSearchHistories() {
        localDataSource.deleteSearchHistories()
    }
}
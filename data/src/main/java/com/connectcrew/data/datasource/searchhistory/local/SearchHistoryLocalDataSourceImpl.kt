package com.connectcrew.data.datasource.searchhistory.local

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.connectcrew.data.database.TeamOneDataBase
import com.connectcrew.data.model.searchhistory.SearchKeyword
import com.connectcrew.data.model.searchhistory.asEntity
import com.connectcrew.domain.usecase.searchhistory.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class SearchHistoryLocalDataSourceImpl @Inject constructor(
    private val teamOneDataBase: TeamOneDataBase
) : SearchHistoryLocalDataSource {

    override suspend fun insertSearchHistory(searchKeyword: SearchKeyword) {
        teamOneDataBase.searchHistoryDao().insertSearchHistory(searchKeyword)
    }

    override fun getSearchHistories(): Flow<PagingData<SearchHistoryEntity>> = Pager(
        config = PagingConfig(pageSize = PAGE_LOAD_SIZE),
        pagingSourceFactory = { teamOneDataBase.searchHistoryDao().getSearchHistories() }
    ).flow.map { pagingData ->
        pagingData.map(SearchKeyword::asEntity)
    }

    override suspend fun deleteSearchHistory(content: String) {
        teamOneDataBase.searchHistoryDao().deleteSearchHistory(content)
    }

    override suspend fun deleteSearchHistories() {
        teamOneDataBase.searchHistoryDao().deleteSearchHistories()
    }

    companion object {
        private const val PAGE_LOAD_SIZE = 50
    }
}
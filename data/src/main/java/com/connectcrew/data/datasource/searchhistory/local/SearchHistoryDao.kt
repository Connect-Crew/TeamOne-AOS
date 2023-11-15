package com.connectcrew.data.datasource.searchhistory.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.connectcrew.data.model.searchhistory.SearchKeyword

@Dao
internal interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchHistory(searchKeyword: SearchKeyword)

    @Query("SELECT * FROM SearchKeyword ORDER BY createdAt DESC")
    fun getSearchHistories(): PagingSource<Int, SearchKeyword>

    @Query("DELETE FROM SearchKeyword WHERE content = :content")
    suspend fun deleteSearchHistory(content: String)

    @Query("DELETE FROM SearchKeyword")
    suspend fun deleteSearchHistories()
}
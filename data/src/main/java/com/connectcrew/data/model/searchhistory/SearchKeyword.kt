package com.connectcrew.data.model.searchhistory

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.connectcrew.domain.usecase.searchhistory.entity.SearchHistoryEntity


@Entity(tableName = "SearchKeyword")
internal data class SearchKeyword(
    @PrimaryKey
    @ColumnInfo(name = "content")
    val content: String,
    @ColumnInfo(name = "createdAt")
    val createdAt: String
)

internal fun SearchKeyword.asEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(
        content = content,
        createdAt = createdAt
    )
}

internal fun SearchHistoryEntity.asExternalModel(): SearchKeyword {
    return SearchKeyword(
        content = content,
        createdAt = createdAt
    )
}
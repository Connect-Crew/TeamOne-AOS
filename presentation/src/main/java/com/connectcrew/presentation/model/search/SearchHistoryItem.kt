package com.connectcrew.presentation.model.search

import com.connectcrew.domain.usecase.searchhistory.entity.SearchHistoryEntity
import java.time.ZonedDateTime

data class SearchHistoryItem(
    val content: String,
    val createdAt: String = ZonedDateTime.now().toString()
)

fun SearchHistoryItem.asEntity(): SearchHistoryEntity {
    return SearchHistoryEntity(
        content = content,
        createdAt = createdAt
    )
}

fun SearchHistoryEntity.asItem(): SearchHistoryItem {
    return SearchHistoryItem(
        content = content,
        createdAt = createdAt
    )
}
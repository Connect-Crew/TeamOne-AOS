package com.connectcrew.domain.usecase.searchhistory.entity

import com.connectcrew.domain.usecase.Entity

data class SearchHistoryEntity(
    val content: String,
    val createdAt: String
) : Entity
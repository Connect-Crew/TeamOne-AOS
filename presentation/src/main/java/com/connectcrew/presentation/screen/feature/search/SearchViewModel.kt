package com.connectcrew.presentation.screen.feature.search

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.connectcrew.domain.usecase.searchhistory.DeleteSearchHistoriesUseCase
import com.connectcrew.domain.usecase.searchhistory.DeleteSearchHistoryUseCase
import com.connectcrew.domain.usecase.searchhistory.GetSearchHistoriesUseCase
import com.connectcrew.domain.usecase.searchhistory.SaveSearchHistoryUseCase
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.search.SearchHistoryItem
import com.connectcrew.presentation.model.search.asEntity
import com.connectcrew.presentation.model.search.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase,
    getSearchHistoriesUseCase: GetSearchHistoriesUseCase,
    private val deleteSearchHistoryUseCase: DeleteSearchHistoryUseCase,
    private val deleteSearchHistoriesUseCase: DeleteSearchHistoriesUseCase
) : BaseViewModel() {

    private val searchingKeyword = savedStateHandle.getStateFlow(KEY_SEARCHING_KEYWORD, "")

    val searchHistories: Flow<PagingData<SearchHistoryItem>> = getSearchHistoriesUseCase(Unit)
        .map { pagingData -> pagingData.map { searchHistoryEntity -> searchHistoryEntity.asItem() } }
        .cachedIn(viewModelScope)
        .combine(searchingKeyword) { pagingData, searchingKeyword -> pagingData.filter { it.content.contains(searchingKeyword) } }

    private fun saveSearchHistory(searchContent: String) {
        viewModelScope.launch {
            saveSearchHistoryUseCase(
                SaveSearchHistoryUseCase.Params(
                    SearchHistoryItem(
                        content = searchContent
                    ).asEntity()
                )
            )
        }
    }

    fun deleteSearchHistories() {
        viewModelScope.launch {
            deleteSearchHistoriesUseCase(Unit)
        }
    }

    fun deleteSearchHistory(searchContent: String) {
        viewModelScope.launch {
            deleteSearchHistoryUseCase(
                DeleteSearchHistoryUseCase.Params(
                    content = searchContent
                )
            )
        }
    }

    fun searchProjectFeed(keyword: String) {
        viewModelScope.launch {
            if (keyword.length >= 2) {
                // TODO::검색 결과 화면으로 전환 로직 필요
                saveSearchHistory(keyword)
            } else {
                setMessage(R.string.search_error)
            }
        }
    }

    fun setSearchingKeyword(text: String) {
        viewModelScope.launch {
            savedStateHandle.set(KEY_SEARCHING_KEYWORD, text)
        }
    }

    companion object {
        private const val KEY_SEARCHING_KEYWORD = "searching_keyword"
    }
}
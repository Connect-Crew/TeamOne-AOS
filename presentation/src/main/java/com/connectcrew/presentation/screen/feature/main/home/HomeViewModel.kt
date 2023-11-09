package com.connectcrew.presentation.screen.feature.main.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.connectcrew.domain.usecase.project.GetProjectFeedsUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.connectcrew.presentation.model.project.ProjectCategoryType
import com.connectcrew.presentation.model.project.ProjectFeed
import com.connectcrew.presentation.model.project.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProjectFeedsUseCase: GetProjectFeedsUseCase,
    signViewModelDelegate: SignViewModelDelegate
) : BaseViewModel(), SignViewModelDelegate by signViewModelDelegate {

    val projectCategoryTypes = flowOf(ProjectCategoryType.values().toList())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projectFeeds: Flow<PagingData<ProjectFeed>> = loadDataSignal
        .combine(userToken, ::Pair)
        .flatMapLatest { (_, token) -> getProjectFeedsUseCase(GetProjectFeedsUseCase.Params(userToken = token)) }
        .mapLatest { pagingData -> pagingData.map(ProjectFeedEntity::asItem) }
        .cachedIn(viewModelScope)
}
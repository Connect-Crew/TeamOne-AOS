package com.connectcrew.presentation.screen.feature.main.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.connectcrew.domain.usecase.project.GetProjectFeedsUseCase
import com.connectcrew.domain.usecase.project.SetProjectFeedLikeUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectFeedEntity
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.UnAuthorizedException
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.project.ProjectCategoryItem
import com.connectcrew.presentation.model.project.ProjectCategoryType
import com.connectcrew.presentation.model.project.ProjectFeed
import com.connectcrew.presentation.model.project.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProjectFeedsUseCase: GetProjectFeedsUseCase,
    private val setProjectFeedLikeUseCase: SetProjectFeedLikeUseCase,
    signViewModelDelegate: SignViewModelDelegate
) : BaseViewModel(), SignViewModelDelegate by signViewModelDelegate {

    private val selectedCategory = savedStateHandle.getStateFlow(KEY_SELECTED_CATEGORY, ProjectCategoryType.CATEGORY_ALL)
    val selectedProjectFeed = savedStateHandle.getStateFlow<ProjectFeed?>(KEY_SELECTED_PROJECT, null)

    val projectCategoryItems = createProjectCategoryFlow()
        .combine(selectedCategory, ::Pair)
        .map { (category, selectedItem) -> category.map { it.copy(isSelected = selectedItem == it.category) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val projectFeeds: Flow<PagingData<ProjectFeed>> = combine(loadDataSignal, userToken, selectedCategory, ::Triple)
        .flatMapLatest { (_, token, category) -> getProjectFeedsUseCase(GetProjectFeedsUseCase.Params(userToken = token, part = category.type)) }
        .mapLatest { pagingData -> pagingData.map(ProjectFeedEntity::asItem) }
        .cachedIn(viewModelScope)

    private val _scrollToCenterForProjectCategory = MutableEventFlow<ProjectCategoryType>()
    val scrollToCenterForProjectCategory: EventFlow<ProjectCategoryType> = _scrollToCenterForProjectCategory

    private val _navigateToProjectFeedDetail = MutableEventFlow<Int>()
    val navigateToProjectFeedDetail: EventFlow<Int> = _navigateToProjectFeedDetail

    private val _navigateToRecruitmentNoticeSummary = MutableEventFlow<Unit>()
    val navigateToRecruitmentNoticeSummary: EventFlow<Unit> = _navigateToRecruitmentNoticeSummary

    fun setSelectedCategory(categoryType: ProjectCategoryType) {
        viewModelScope.launch {
            if (selectedCategory.value == categoryType) return@launch
            savedStateHandle.set(KEY_SELECTED_CATEGORY, categoryType)
            _scrollToCenterForProjectCategory.emit(categoryType)
        }
    }

    fun setProjectFeedLike(projectFeed: ProjectFeed) {
        viewModelScope.launch {
            setProjectFeedLikeUseCase(
                SetProjectFeedLikeUseCase.Params(
                    userToken = userToken.value,
                    projectId = projectFeed.id
                )
            ).asResult().onEach {
                setLoading(it is ApiResult.Loading)
            }.collect {
                when (it) {
                    is ApiResult.Loading -> return@collect
                    is ApiResult.Success -> {
                        //TODO:: 좋아요 클릭 성공 시 피드 업데이트 로직 작성
                    }

                    is ApiResult.Error -> {
                        when (it.exception) {
                            is IOException -> setMessage(R.string.network_error)
                            is TeamOneException -> when (it.exception) {
                                is UnAuthorizedException -> refreshUserToken()
                                else -> setMessage(it.exception.message.toString())
                            }

                            else -> setMessage(R.string.unknown_error)
                        }
                    }
                }
            }
        }
    }

    fun navigateToProjectFeedDetail(projectFeed: ProjectFeed) {
        viewModelScope.launch {
            _navigateToProjectFeedDetail.emit(projectFeed.id)
        }
    }

    fun navigateToRecruitmentNoticeSummary(projectFeed: ProjectFeed) {
        viewModelScope.launch {
            savedStateHandle.set(KEY_SELECTED_PROJECT, projectFeed)
            _navigateToRecruitmentNoticeSummary.emit(Unit)
        }
    }

    private fun createProjectCategoryFlow() = flow {
        emit(ProjectCategoryType.values().toList().map { ProjectCategoryItem(it, false) })
    }

    companion object {
        private const val KEY_SELECTED_CATEGORY = "selected_category"
        private const val KEY_SELECTED_PROJECT = "selected_project"
    }
}
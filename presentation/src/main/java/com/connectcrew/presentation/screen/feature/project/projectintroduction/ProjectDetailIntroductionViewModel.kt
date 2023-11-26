package com.connectcrew.presentation.screen.feature.project.projectintroduction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.GetProjectFeedDetailUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.UnAuthorizedException
import com.connectcrew.domain.util.asResult
import com.connectcrew.domain.util.data
import com.connectcrew.domain.util.succeeded
import com.connectcrew.presentation.model.project.ProjectFeedDetail
import com.connectcrew.presentation.model.project.RecruitStatus
import com.connectcrew.presentation.model.project.asItem
import com.connectcrew.presentation.model.user.User
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailIntroductionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProjectFeedDetailUseCase: GetProjectFeedDetailUseCase,
    signViewModelDelegate: SignViewModelDelegate
) : BaseViewModel(), SignViewModelDelegate by signViewModelDelegate {

    private val projectId = savedStateHandle.getStateFlow<Int?>(KEY_PROJECT_ID, null)

    private val projectFeedDetailResult = combine(loadDataSignal, userToken, projectId, ::Triple)
        .filter { it.third != null }
        .flatMapLatest { (_, token, id) -> getProjectFeedDetailUseCase(GetProjectFeedDetailUseCase.Params(token, id!!)).asResult() }
        .onEach { if (it.succeeded) _projectDetail.value = it.data!!.asItem() }
        .stateIn(viewModelScope, SharingStarted.Lazily, ApiResult.Loading)

    private val _projectDetail = MutableStateFlow<ProjectFeedDetail?>(null)
    val projectDetail: StateFlow<ProjectFeedDetail?> = _projectDetail

    private val _projectFeedDetailUiState = MutableStateFlow(InitializerUiState.Loading)
    val projectFeedDetailUiState: StateFlow<InitializerUiState> = _projectFeedDetailUiState

    private val _navigateToProjectEnrollDialog = MutableEventFlow<ProjectFeedDetail>()
    val navigateToProjectEnrollDialog: EventFlow<ProjectFeedDetail> = _navigateToProjectEnrollDialog

    init {
        viewModelScope.launch {
            projectFeedDetailResult.collect {
                when (it) {
                    is ApiResult.Loading -> _projectFeedDetailUiState.value = InitializerUiState.Loading
                    is ApiResult.Success -> _projectFeedDetailUiState.value = InitializerUiState.Success
                    is ApiResult.Error -> when (it.exception) {
                        is TeamOneException -> when (it.exception) {
                            is UnAuthorizedException -> refreshUserToken()
                            else -> _projectFeedDetailUiState.value = InitializerUiState.Error
                        }

                        else -> _projectFeedDetailUiState.value = InitializerUiState.Error
                    }
                }
            }
        }
    }

    fun setProjectId(projectId: Int) {
        savedStateHandle.set(KEY_PROJECT_ID, projectId)
    }

    fun createProjectDetailIntroductionUiModels(projectFeedDetail: ProjectFeedDetail): List<ProjectDetailIntroductionUiModel> {
        return listOf(
            // 프로젝트 대표 이미지
            ProjectDetailIntroductionUiModel.ProjectDetailIntroductionBannerUiModel(projectFeedDetail.bannerImageUrls),

            // 프로젝트 제목
            ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTitleUiModel(
                region = projectFeedDetail.region,
                isOnline = projectFeedDetail.isOnline,
                title = projectFeedDetail.title,
                createdAt = projectFeedDetail.createdAt,
                careerMin = projectFeedDetail.careerMin,
                projectState = projectFeedDetail.state,
                category = projectFeedDetail.category
            ),

            // 리더 정보
            ProjectDetailIntroductionUiModel.ProjectDetailIntroductionLeaderUiModel(projectFeedDetail.leader),

            // 지원 공고
            ProjectDetailIntroductionUiModel.ProjectDetailIntroductionRecruitmentNoticeUiModel(
                projectFeedDetail.recruitStatus,
                projectFeedDetail.totalCurrentCount,
                projectFeedDetail.totalMaxCount,
                projectFeedDetail.isEnroll
            ),

            // 상세 설명
            ProjectDetailIntroductionUiModel.ProjectDetailIntroductionDescriptionUiModel(projectFeedDetail.projectIntroduction),

            // 기술 스택
            ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTechStack(projectFeedDetail.skills)
        )
    }

    fun navigateToProjectEnrollDialog() {
        viewModelScope.launch {
            projectDetail.value?.let { _navigateToProjectEnrollDialog.emit(it) }
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
    }
}


sealed class ProjectDetailIntroductionUiModel {

    data class ProjectDetailIntroductionBannerUiModel(
        val imageUrls: List<String>
    ) : ProjectDetailIntroductionUiModel()

    data class ProjectDetailIntroductionTitleUiModel(
        val region: String,
        val isOnline: Boolean,
        val title: String,
        val createdAt: String,
        val careerMin: String,
        val projectState: String,
        val category: List<String>
    ) : ProjectDetailIntroductionUiModel()

    data class ProjectDetailIntroductionLeaderUiModel(
        val leaderInfo: User
    ) : ProjectDetailIntroductionUiModel()

    data class ProjectDetailIntroductionRecruitmentNoticeUiModel(
        val recruitStatus: List<RecruitStatus>,
        val totalCurrentCount: Int,
        val totalMaxCount: Int,
        val isEnroll: Boolean
    ) : ProjectDetailIntroductionUiModel()

    data class ProjectDetailIntroductionDescriptionUiModel(
        val projectDescription: String
    ) : ProjectDetailIntroductionUiModel()

    data class ProjectDetailIntroductionTechStack(
        val techStacks: List<String>
    ) : ProjectDetailIntroductionUiModel()
}
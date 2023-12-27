package com.connectcrew.presentation.screen.feature.project.projectintroduction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.GetProjectFeedDetailUseCase
import com.connectcrew.domain.usecase.project.SetProjectFeedLikeUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.asResult
import com.connectcrew.domain.util.data
import com.connectcrew.domain.util.succeeded
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.project.ProjectFeedDetail
import com.connectcrew.presentation.model.project.RecruitStatus
import com.connectcrew.presentation.model.project.asItem
import com.connectcrew.presentation.model.project.toSummary
import com.connectcrew.presentation.model.user.User
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.delegate.ProjectFeedViewModelDelegate
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ProjectDetailIntroductionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProjectFeedDetailUseCase: GetProjectFeedDetailUseCase,
    private val setProjectFeedLikeUseCase: SetProjectFeedLikeUseCase,
    projectFeedViewModelDelegate: ProjectFeedViewModelDelegate
) : BaseViewModel(), ProjectFeedViewModelDelegate by projectFeedViewModelDelegate {

    private val projectId = savedStateHandle.getStateFlow<Long?>(KEY_PROJECT_ID, null)

    private val projectFeedDetailResult = combine(loadDataSignal, projectId, ::Pair)
        .filter { it.second != null }
        .flatMapLatest { (_, id) -> getProjectFeedDetailUseCase(GetProjectFeedDetailUseCase.Params(id!!)).asResult() }
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
                    is ApiResult.Error -> _projectFeedDetailUiState.value = InitializerUiState.Error
                }
            }
        }
    }

    fun setProjectId(projectId: Long) {
        savedStateHandle.set(KEY_PROJECT_ID, projectId)
    }

    fun setProjectFeedLike() {
        viewModelScope.launch {
            setProjectFeedLikeUseCase(SetProjectFeedLikeUseCase.Params(projectId.value ?: -1))
                .asResult()
                .onEach { setLoading(it is ApiResult.Loading) }
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> {
                            val projectFeedLikeInfo = it.data.asItem()

                            _projectDetail.update {
                                it?.copy(
                                    isLike = projectFeedLikeInfo.isLike,
                                    likeCount = projectFeedLikeInfo.likeCount
                                )?.also { feedDetail ->
                                    updateProjectFeedAction(projectFeed = feedDetail.toSummary(), ZonedDateTime.now())
                                }
                            }
                        }

                        is ApiResult.Error -> when (it.exception) {
                            is IOException -> setMessage(R.string.network_error)
                            is TeamOneException -> setMessage(it.exception.message.toString())
                            else -> setMessage(R.string.unknown_error)
                        }
                    }
                }
        }
    }

    fun createProjectDetailIntroductionUiModels(projectFeedDetail: ProjectFeedDetail): List<ProjectDetailIntroductionUiModel> {
        val projectDetailIntroductionUiModels = mutableListOf<ProjectDetailIntroductionUiModel>()

        return projectDetailIntroductionUiModels.apply {
            // 프로젝트 대표 이미지
            add(ProjectDetailIntroductionUiModel.ProjectDetailIntroductionBannerUiModel(projectFeedDetail.bannerImageUrls))

            // 프로젝트 제목
            add(
                ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTitleUiModel(
                    region = projectFeedDetail.region,
                    isOnline = projectFeedDetail.isOnline,
                    title = projectFeedDetail.title,
                    createdAt = projectFeedDetail.createdAt,
                    careerMin = projectFeedDetail.careerMin,
                    projectState = projectFeedDetail.state,
                    category = projectFeedDetail.category
                )
            )

            // 리더 정보
            add(ProjectDetailIntroductionUiModel.ProjectDetailIntroductionLeaderUiModel(projectFeedDetail.leader))

            // 지원 공고
            add(
                ProjectDetailIntroductionUiModel.ProjectDetailIntroductionRecruitmentNoticeUiModel(
                    recruitStatus = projectFeedDetail.recruitStatus,
                    totalCurrentCount = projectFeedDetail.totalCurrentCount,
                    totalMaxCount = projectFeedDetail.totalMaxCount,
                    isEnroll = projectFeedDetail.isEnroll
                )
            )

            // 상세 설명
            add(ProjectDetailIntroductionUiModel.ProjectDetailIntroductionDescriptionUiModel(projectFeedDetail.projectIntroduction))

            // 기술 스택
            if (projectFeedDetail.skills.first().isNotEmpty()) {
                add(ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTechStack(projectFeedDetail.skills))
            }
        }
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
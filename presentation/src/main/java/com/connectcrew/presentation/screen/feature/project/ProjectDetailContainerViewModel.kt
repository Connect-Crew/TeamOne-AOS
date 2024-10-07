package com.connectcrew.presentation.screen.feature.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.CreateProjectReportUseCase
import com.connectcrew.domain.usecase.project.DeleteProjectFeedUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.project.ProjectFeedDetail
import com.connectcrew.presentation.model.project.ProjectFeedDetailCategory
import com.connectcrew.presentation.model.project.toSummary
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.delegate.ProjectFeedViewModelDelegate
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.ZonedDateTime
import javax.inject.Inject

@HiltViewModel
class ProjectDetailContainerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createProjectReportUseCase: CreateProjectReportUseCase,
    private val deleteProjectFeedUseCase: DeleteProjectFeedUseCase,
    projectFeedViewModelDelegate: ProjectFeedViewModelDelegate
) : BaseViewModel(), ProjectFeedViewModelDelegate by projectFeedViewModelDelegate {

    val projectId = savedStateHandle.getStateFlow<Long?>(KEY_PROJECT_ID, null)
    val isProjectLeader = savedStateHandle.getStateFlow<Boolean?>(KEY_IS_PROJECT_LEADER, null)

    val projectReportReason = savedStateHandle.getStateFlow(KEY_PROJECT_REPORT_REASON, "")
    val projectFeedDetail
        get() = savedStateHandle.get<ProjectFeedDetail?>(KEY_PROJECT_DETAIL)

    private val selectedProjectDetailCategory = savedStateHandle.getStateFlow(KEY_PROJECT_DETAIL_CATEGORY, ProjectFeedDetailCategory.INTRODUCTION)

    private val _invalidateProjectDetail = MutableEventFlow<Unit>()
    val invalidateProjectDetail: EventFlow<Unit> = _invalidateProjectDetail

    private val _navigateToProjectUpdate = MutableEventFlow<ProjectFeedDetail>()
    val navigateToProjectUpdate: EventFlow<ProjectFeedDetail> = _navigateToProjectUpdate

    private val _navigateToProjectRemovePopup = MutableEventFlow<Long>()
    val navigateToProjectRemovePopup: EventFlow<Long> = _navigateToProjectRemovePopup

    private val _navigateToProjectRecruit = MutableEventFlow<Long>()
    val navigateToProjectRecruit: EventFlow<Long> = _navigateToProjectRecruit

    private val _navigateToProjectCompletedPopup = MutableEventFlow<Long>()
    val navigateToProjectCompletedPopup: EventFlow<Long> = _navigateToProjectCompletedPopup

    private val _navigateToProjectReportReasonDialog = MutableEventFlow<Unit>()
    val navigateToProjectReportReasonDialog: EventFlow<Unit> = _navigateToProjectReportReasonDialog

    private val _navigateToProjectReportCompletedDialog = MutableEventFlow<Unit>()
    val navigateToProjectReportCompletedDialog: EventFlow<Unit> = _navigateToProjectReportCompletedDialog

    private val _navigateToBack = MutableEventFlow<Unit>()
    val navigateToBack: EventFlow<Unit> = _navigateToBack

    fun setProjectLeader(isLeader: Boolean) {
        if (isProjectLeader.value == isLeader) return
        savedStateHandle.set(KEY_IS_PROJECT_LEADER, isLeader)
    }

    fun setProjectFeedDetail(projectFeedDetail: ProjectFeedDetail) {
        savedStateHandle.set(KEY_PROJECT_DETAIL, projectFeedDetail)
    }

    fun setSelectedProjectDetailCategory(category: ProjectFeedDetailCategory) {
        if (selectedProjectDetailCategory.value == category) return
        savedStateHandle.set(KEY_PROJECT_DETAIL_CATEGORY, category)
    }

    fun setProjectReportReason(reason: String) {
        savedStateHandle.set(KEY_PROJECT_REPORT_REASON, reason)
    }

    fun navigateToProjectUpdate() {
        viewModelScope.launch {
            projectFeedDetail?.let { _navigateToProjectUpdate.emit(it) }
        }
    }

    fun navigateToProjectRemovePopup() {
        viewModelScope.launch {
            _navigateToProjectRemovePopup.emit(projectId.value!!)
        }
    }

    fun navigateToProjectCompletedPopup() {
        viewModelScope.launch {
            _navigateToProjectCompletedPopup.emit(projectId.value!!)
        }
    }

    fun navigateToProjectRecruit() {
        viewModelScope.launch {
            _navigateToProjectRecruit.emit(projectId.value!!)
        }
    }

    fun navigateToProjectReportReasonDialog() {
        viewModelScope.launch {
            _navigateToProjectReportReasonDialog.emit(Unit)
        }
    }

    fun navigateToProjectReportCompletedDialog() {
        viewModelScope.launch {
            createProjectReportUseCase(CreateProjectReportUseCase.Params(projectId.value ?: -1, projectReportReason.value))
                .asResult()
                .onEach { setLoading(it is ApiResult.Loading) }
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> {
                            _navigateToProjectReportCompletedDialog.emit(Unit)
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

    fun deleteAndNavigateToBack() {
        viewModelScope.launch {
            deleteProjectFeedUseCase(DeleteProjectFeedUseCase.Params(projectId.value!!))
                .asResult()
                .onEach { setLoading(it is ApiResult.Loading) }
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> {
                            deleteProjectFeedAction(projectFeedDetail?.toSummary(), ZonedDateTime.now())
                            _navigateToBack.emit(Unit)
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

    fun invalidateProjectDetail() {
        viewModelScope.launch {
            _invalidateProjectDetail.emit(Unit)
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_DETAIL = "project_detail"
        private const val KEY_PROJECT_DETAIL_CATEGORY = "project_detail_category"
        private const val KEY_PROJECT_REPORT_REASON = "project_report_reason"

        private const val KEY_IS_PROJECT_LEADER = "is_project_leader"
    }
}
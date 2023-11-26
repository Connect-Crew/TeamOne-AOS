package com.connectcrew.presentation.screen.feature.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.CreateProjectReportUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.UnAuthorizedException
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.project.ProjectFeedDetail
import com.connectcrew.presentation.model.project.ProjectFeedDetailCategory
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProjectDetailContainerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val createProjectReportUseCase: CreateProjectReportUseCase,
    signViewModelDelegate: SignViewModelDelegate
) : BaseViewModel(), SignViewModelDelegate by signViewModelDelegate {

    val projectId = savedStateHandle.getStateFlow<Int?>(KEY_PROJECT_ID, null)

    private val selectedProjectDetailCategory =
        savedStateHandle.getStateFlow(KEY_PROJECT_DETAIL_CATEGORY, ProjectFeedDetailCategory.INTRODUCTION)

    val projectReportReason = savedStateHandle.getStateFlow(KEY_PROJECT_REPORT_REASON, "")

    private val _invalidateProjectDetail = MutableEventFlow<Unit>()
    val invalidateProjectDetail: EventFlow<Unit> = _invalidateProjectDetail

    private val _navigateToProjectEnrollDialog = MutableEventFlow<Pair<Int, ProjectFeedDetail>>()
    val navigateToProjectEnrollDialog: EventFlow<Pair<Int, ProjectFeedDetail>> = _navigateToProjectEnrollDialog

    private val _navigateToProjectReportReasonDialog = MutableEventFlow<Unit>()
    val navigateToProjectReportReasonDialog: EventFlow<Unit> = _navigateToProjectReportReasonDialog

    private val _navigateToProjectReportCompletedDialog = MutableEventFlow<Unit>()
    val navigateToProjectReportCompletedDialog: EventFlow<Unit> = _navigateToProjectReportCompletedDialog

    fun setSelectedProjectDetailCategory(category: ProjectFeedDetailCategory) {
        if (selectedProjectDetailCategory.value == category) return
        savedStateHandle.set(KEY_PROJECT_DETAIL_CATEGORY, category)
    }

    fun setProjectReportReason(reason: String) {
        savedStateHandle.set(KEY_PROJECT_REPORT_REASON, reason)
    }

    fun navigateToProjectEnrollDialog(projectFeedDetail: ProjectFeedDetail) {
        viewModelScope.launch {
            _navigateToProjectEnrollDialog.emit(projectId.value!! to projectFeedDetail)
        }
    }

    fun navigateToProjectReportReasonDialog() {
        viewModelScope.launch {
            _navigateToProjectReportReasonDialog.emit(Unit)
        }
    }

    fun navigateToProjectReportCompletedDialog() {
        viewModelScope.launch {
            createProjectReportUseCase(CreateProjectReportUseCase.Params(userToken.value, projectId.value ?: -1, projectReportReason.value))
                .asResult()
                .onEach { setLoading(it is ApiResult.Loading) }
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> _navigateToProjectReportCompletedDialog.emit(Unit)
                        is ApiResult.Error -> when (it.exception) {
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

    fun invalidateProjectDetail() {
        viewModelScope.launch {
            _invalidateProjectDetail.emit(Unit)
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_DETAIL_CATEGORY = "project_detail_category"
        private const val KEY_PROJECT_REPORT_REASON = "project_report_reason"
    }
}
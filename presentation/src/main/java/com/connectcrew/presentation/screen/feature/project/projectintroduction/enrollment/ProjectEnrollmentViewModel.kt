package com.connectcrew.presentation.screen.feature.project.projectintroduction.enrollment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.SetProjectEnrollmentUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.project.ProjectFeedDetail
import com.connectcrew.presentation.model.project.RecruitStatus
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ProjectEnrollmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val setProjectEnrollmentUseCase: SetProjectEnrollmentUseCase
) : BaseViewModel() {

    private val projectId
        get() = savedStateHandle.get<Long>(KEY_PROJECT_ID)

    val selectedRecruitPart
        get() = savedStateHandle.get<RecruitStatus>(KEY_SELECTED_PART)

    val projectFeedDetail = savedStateHandle.getStateFlow<ProjectFeedDetail?>(KEY_PROJECT_FEED_DETAIL, null)
    val enrollmentReason = savedStateHandle.getStateFlow(KEY_PROJECT_ENROLLMENT_REASON, "")
    val enrollmentContact = savedStateHandle.getStateFlow(KEY_PROJECT_ENROLLMENT_CONTACT, "")

    private val _navigateToProjectEnrollmentReasonDialog = MutableEventFlow<Unit>()
    val navigateToProjectEnrollmentReasonDialog: EventFlow<Unit> = _navigateToProjectEnrollmentReasonDialog

    private val _navigateToProjectEnrollmentCompletedDialog = MutableEventFlow<Unit>()
    val navigateToProjectEnrollmentCompletedDialog: EventFlow<Unit> = _navigateToProjectEnrollmentCompletedDialog

    fun setEnrollmentReason(text: String) {
        savedStateHandle.set(KEY_PROJECT_ENROLLMENT_REASON, text)
    }

    fun setEnrollContact(text: String) {
        if (enrollmentContact.value == text) return
        savedStateHandle.set(KEY_PROJECT_ENROLLMENT_CONTACT, text)
    }

    fun navigateToProjectEnrollmentReasonDialog(recruitStatus: RecruitStatus) {
        viewModelScope.launch {
            savedStateHandle.set(KEY_SELECTED_PART, recruitStatus)
            _navigateToProjectEnrollmentReasonDialog.emit(Unit)
        }
    }

    fun setProjectEnrollment() {
        viewModelScope.launch {
            setProjectEnrollmentUseCase(
                SetProjectEnrollmentUseCase.Params(
                    projectId = projectId!!,
                    enrollmentPart = selectedRecruitPart?.partKey!!,
                    enrollmentReason = enrollmentReason.value,
                    enrollmentContact = enrollmentContact.value
                )
            ).asResult()
                .onEach { setLoading(it is ApiResult.Loading) }
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> _navigateToProjectEnrollmentCompletedDialog.emit(Unit)
                        is ApiResult.Error -> when (it.exception) {
                            is IOException -> setMessage(R.string.network_error)
                            is TeamOneException -> setMessage(it.exception.message.toString())
                            else -> setMessage(R.string.unknown_error)
                        }
                    }
                }
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_FEED_DETAIL = "project_feed_detail"
        private const val KEY_PROJECT_ENROLLMENT_REASON = "project_enrollment_reason"
        private const val KEY_PROJECT_ENROLLMENT_CONTACT = "project_enrollment_contact"
        private const val KEY_SELECTED_PART = "selected_part"
    }
}
package com.connectcrew.presentation.screen.feature.project.projectintroduction.enrollmentmanagment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.GetProjectEnrollmentPartMembersUseCase
import com.connectcrew.domain.usecase.project.SetProjectEnrollmentPartMemberUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentPartMemberEntity
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.asResult
import com.connectcrew.domain.util.data
import com.connectcrew.domain.util.succeeded
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.project.ProjectEnrollmentMember
import com.connectcrew.presentation.model.project.ProjectEnrollmentPartMember
import com.connectcrew.presentation.model.project.ProjectEnrollmentState
import com.connectcrew.presentation.model.project.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.IOException
import javax.inject.Inject

@HiltViewModel
class ProjectEnrollmentPartsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProjectEnrollmentPartMembersUseCase: GetProjectEnrollmentPartMembersUseCase,
    private val setProjectEnrollmentPartMemberUseCase: SetProjectEnrollmentPartMemberUseCase
) : BaseViewModel() {

    private val projectId
        get() = savedStateHandle.get<Long>(KEY_PROJECT_ID) ?: -1

    val projectEnrollmentMember
        get() = savedStateHandle.get<ProjectEnrollmentMember>(KEY_PROJECT_ENROLLMENT_MEMBER)

    private val projectEnrollmentPartMembersResult = loadDataSignal
        .flatMapLatest { getProjectEnrollmentPartMembersUseCase(GetProjectEnrollmentPartMembersUseCase.Params(projectId, projectEnrollmentMember!!.partKey)).asResult() }
        .onEach {
            if (it.succeeded) _projectEnrollmentPartMembers.value = it.data
                ?.map(ProjectEnrollmentPartMemberEntity::asItem)
                ?.sortedBy { it.state != ProjectEnrollmentState.WAITING }
                ?: emptyList()
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ApiResult.Loading)

    val projectEnrollmentPartMembersUiState = projectEnrollmentPartMembersResult
        .mapLatest {
            when (it) {
                is ApiResult.Loading -> if (swipeRefreshing.value) InitializerUiState.Success else InitializerUiState.Loading
                is ApiResult.Success -> InitializerUiState.Success.also { isSwipeRefreshing(false) }
                is ApiResult.Error -> InitializerUiState.Error.also { isSwipeRefreshing(false) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitializerUiState.Loading)

    private val _projectEnrollmentPartMembers = MutableStateFlow<List<ProjectEnrollmentPartMember>>(emptyList())
    val projectEnrollmentPartMembers: StateFlow<List<ProjectEnrollmentPartMember>> = _projectEnrollmentPartMembers

    private val _navigateToRejectPopup = MutableEventFlow<Pair<Int, String>>()
    val navigateToRejectPopup: EventFlow<Pair<Int, String>> = _navigateToRejectPopup

    private val _navigateToPassedPopup = MutableEventFlow<Pair<Int, String>>()
    val navigateToPassedPopup: EventFlow<Pair<Int, String>> = _navigateToPassedPopup

    private val _navigateToProfile = MutableEventFlow<Int>()
    val navigateToProfile: EventFlow<Int> = _navigateToProfile

    fun setEnrollmentUserStatePopup(applyId: Int, isPassed: Boolean, leaderMessage: String? = null) {
        viewModelScope.launch {
            setProjectEnrollmentPartMemberUseCase(SetProjectEnrollmentPartMemberUseCase.Params(applyId, isPassed, leaderMessage))
                .asResult()
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> onRefresh()
                        is ApiResult.Error -> when (it.exception) {
                            is IOException -> setMessage(R.string.network_error)
                            is TeamOneException -> setMessage(it.exception.message.toString())
                            else -> setMessage(R.string.unknown_error)
                        }
                    }
                }
        }
    }

    fun navigateToUpdateEnrollmentUserStatePopup(isPassed: Boolean, enrollmentInfo: Pair<Int, String>) {
        viewModelScope.launch {
            if (isPassed) {
                _navigateToPassedPopup.emit(enrollmentInfo)
            } else {
                _navigateToRejectPopup.emit(enrollmentInfo)
            }
        }
    }

    fun navigateToProfile(userId: Int) {
        viewModelScope.launch {
            _navigateToProfile.emit(userId)
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_ENROLLMENT_MEMBER = "project_enrollment_member"
    }
}
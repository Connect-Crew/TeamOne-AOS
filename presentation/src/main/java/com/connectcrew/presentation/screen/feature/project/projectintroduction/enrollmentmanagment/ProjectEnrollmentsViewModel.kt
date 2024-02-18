package com.connectcrew.presentation.screen.feature.project.projectintroduction.enrollmentmanagment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.GetProjectEnrollmentMembersUseCase
import com.connectcrew.domain.usecase.project.entity.ProjectEnrollmentMemberEntity
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.asResult
import com.connectcrew.domain.util.data
import com.connectcrew.domain.util.succeeded
import com.connectcrew.presentation.model.project.ProjectEnrollmentMember
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
import javax.inject.Inject

@HiltViewModel
class ProjectEnrollmentsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProjectEnrollmentMembersUseCase: GetProjectEnrollmentMembersUseCase
) : BaseViewModel() {

    private val projectId
        get() = savedStateHandle.get<Long>(KEY_PROJECT_ID) ?: -1

    private val projectEnrollmentMembersResult = loadDataSignal
        .flatMapLatest { getProjectEnrollmentMembersUseCase(GetProjectEnrollmentMembersUseCase.Params(projectId)).asResult() }
        .onEach { if (it.succeeded) _enrollmentMembers.value = it.data!!.map(ProjectEnrollmentMemberEntity::asItem) }
        .stateIn(viewModelScope, SharingStarted.Lazily, ApiResult.Loading)

    val projectEnrollmentsUiState = projectEnrollmentMembersResult
        .mapLatest {
            when (it) {
                is ApiResult.Loading -> InitializerUiState.Loading
                is ApiResult.Success -> InitializerUiState.Success
                is ApiResult.Error -> InitializerUiState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ApiResult.Loading)

    private val _enrollmentMembers = MutableStateFlow<List<ProjectEnrollmentMember>>(emptyList())
    val enrollmentMembers: StateFlow<List<ProjectEnrollmentMember>> = _enrollmentMembers

    private val _navigateToEnrollmentPart = MutableEventFlow<Pair<Long, ProjectEnrollmentMember>>()
    val navigateToEnrollmentPart: EventFlow<Pair<Long, ProjectEnrollmentMember>> = _navigateToEnrollmentPart

    fun navigateToEnrollmentPart(projectEnrollmentMember: ProjectEnrollmentMember) {
        viewModelScope.launch {
            _navigateToEnrollmentPart.emit(projectId to projectEnrollmentMember)
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
    }
}
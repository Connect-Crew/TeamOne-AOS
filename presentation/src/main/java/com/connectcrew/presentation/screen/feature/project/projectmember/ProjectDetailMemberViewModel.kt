package com.connectcrew.presentation.screen.feature.project.projectmember

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.GetProjectMembersUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.asResult
import com.connectcrew.domain.util.data
import com.connectcrew.domain.util.succeeded
import com.connectcrew.presentation.model.project.ProjectMember
import com.connectcrew.presentation.model.project.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.WhileViewSubscribed
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailMemberViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProjectMembersUseCase: GetProjectMembersUseCase
) : BaseViewModel() {

    val projectId = savedStateHandle.getStateFlow<Long?>(KEY_PROJECT_ID, null)

    private val isProjectLeader = savedStateHandle.getStateFlow<Boolean?>(KEY_IS_PROJECT_LEADER, null)

    private val projectMembersResult = combine(loadDataSignal, projectId, ::Pair)
        .filter { it.second != null }
        .flatMapLatest { (_, id) -> getProjectMembersUseCase(GetProjectMembersUseCase.Param(id!!)).asResult() }
        .onEach {
            if (it.succeeded) _projectMembers.value = it.data!!.mapIndexed { index, projectMemberEntity ->
                projectMemberEntity.asItem(if (index == 0) false else (isProjectLeader.value ?: false))
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, ApiResult.Loading)

    val projectDetailMemberUiState: StateFlow<InitializerUiState> = projectMembersResult
        .mapLatest {
            when (it) {
                is ApiResult.Loading -> InitializerUiState.Loading
                is ApiResult.Success -> InitializerUiState.Success
                is ApiResult.Error -> InitializerUiState.Error
            }
        }
        .stateIn(viewModelScope, WhileViewSubscribed, InitializerUiState.Loading)

    private val _projectMembers = MutableStateFlow<List<ProjectMember>>(emptyList())
    val projectMembers: StateFlow<List<ProjectMember>> = _projectMembers

    private val _navigateToMemberProfile = MutableEventFlow<ProjectMember>()
    val navigateToMemberProfile: EventFlow<ProjectMember> = _navigateToMemberProfile

    private val _navigateToMemberKickDialog = MutableEventFlow<ProjectMember>()
    val navigateToMemberKickDialog: EventFlow<ProjectMember> = _navigateToMemberKickDialog

    fun setProjectId(projectId: Long) {
        savedStateHandle.set(KEY_PROJECT_ID, projectId)
    }

    fun setProjectLeader(isLeader: Boolean) {
        if (isProjectLeader.value == isLeader) return
        savedStateHandle.set(KEY_IS_PROJECT_LEADER, isLeader)
    }

    fun navigateToMemberProfile(member: ProjectMember) {
        viewModelScope.launch {
            _navigateToMemberProfile.emit(member)
        }
    }

    fun navigateToMemberKickDialog(member: ProjectMember) {
        viewModelScope.launch {
            _navigateToMemberKickDialog.emit(member)
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_IS_PROJECT_LEADER = "is_project_leader"
    }
}
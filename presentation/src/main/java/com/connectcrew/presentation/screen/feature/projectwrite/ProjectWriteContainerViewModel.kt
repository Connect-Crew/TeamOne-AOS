package com.connectcrew.presentation.screen.feature.projectwrite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.GetProjectInfoUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.asResult
import com.connectcrew.domain.util.data
import com.connectcrew.domain.util.succeeded
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.project.ProjectInfo
import com.connectcrew.presentation.model.project.ProjectInfoContainer
import com.connectcrew.presentation.model.project.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.EditTextState
import com.connectcrew.presentation.util.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProjectWriteContainerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val projectInfoUseCase: GetProjectInfoUseCase
) : BaseViewModel() {

    private val projectInfoResult = loadDataSignal
        .flatMapLatest { projectInfoUseCase(Unit).asResult() }
        .onEach { if (it.succeeded) _projectContainerInfo.value = it.data?.asItem() }
        .stateIn(viewModelScope, SharingStarted.Lazily, ApiResult.Loading)

    private val _projectContainerInfo = MutableStateFlow<ProjectInfoContainer?>(null)
    val projectContainerInfo: StateFlow<ProjectInfoContainer?> = _projectContainerInfo

    val projectWriteInitializerUiState = combine(projectInfoResult, projectContainerInfo, ::Pair)
        .map { (apiResult, item) ->
            when (apiResult) {
                is ApiResult.Loading -> if (item == null) InitializerUiState.Loading else InitializerUiState.Success
                is ApiResult.Success -> InitializerUiState.Success
                is ApiResult.Error -> InitializerUiState.Error
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), InitializerUiState.Loading)

    val projectProgress = savedStateHandle.getStateFlow(KEY_PROJECT_WRITE_STEP, 0)

    // region Step 1
    val projectStep1State = projectProgress
        .map { getProjectState(1, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectTitle = savedStateHandle.getStateFlow(KEY_PROJECT_WRITE_TITLE, "")
    val projectTitleEditTextState = savedStateHandle.getStateFlow<EditTextState>(KEY_PROJECT_WRITE_TITLE_EDIT_TEXT_STATE, EditTextState.Loading)
    val enableProjectTitle = projectTitleEditTextState
        .map { it.Success }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    // endregion Step 1

    // region Step 2
    val projectStep2State = projectProgress
        .map { getProjectState(2, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectProgressState = savedStateHandle.getStateFlow<ProjectWriteProgressState?>(KEY_PROJECT_WRITE_PERIOD_PROGRESS_STATE, null)
    val projectLocationType = savedStateHandle.getStateFlow<ProjectWriteLocationType?>(KEY_PROJECT_WRITE_LOCATION_TYPE, null)
    val projectLocation = savedStateHandle.getStateFlow<ProjectInfo?>(KEY_PROJECT_WRITE_LOCATION, null)
    val enableProjectPeriodAndLocation = combine(projectProgressState, projectLocationType, projectLocation, ::Triple)
        .map { (it.first != null && it.second != null && (if (it.second != ProjectWriteLocationType.TYPE_ONLINE) it.third != null else true)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    // endregion Step 2

    // region Step 3
    val projectStep3State = projectProgress
        .map { getProjectState(3, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)
    // endregion Step 3

    // region Step 4
    val projectStep4State = projectProgress
        .map { getProjectState(4, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)
    // endregion Step 4

    // region Step 5
    val projectStep5State = projectProgress
        .map { getProjectState(5, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)
    // endregion Step 5

    fun setWriteProgress(progress: Int) {
        if (projectProgress.value == progress) return
        savedStateHandle.set(KEY_PROJECT_WRITE_STEP, progress)
    }

    fun setProjectTitle(title: String) {
        if (projectTitle.value == title) return
        savedStateHandle.set(KEY_PROJECT_WRITE_TITLE, title)

        if (projectTitle.value.length < 2) {
            setProjectTitleEditTextState(EditTextState.Error(R.string.project_write_title_error))
        } else {
            setProjectTitleEditTextState(EditTextState.Success)
        }
    }

    private fun setProjectTitleEditTextState(editTextState: EditTextState) {
        if (projectTitleEditTextState.value == editTextState) return
        savedStateHandle.set(KEY_PROJECT_WRITE_TITLE_EDIT_TEXT_STATE, editTextState)
    }

    private fun getProjectState(selectProgress: Int, currentProgress: Int): ProjectWriteProgressState {
        return when {
            selectProgress == currentProgress -> ProjectWriteProgressState.STATE_PROCEEDING
            currentProgress > selectProgress -> ProjectWriteProgressState.STATE_PROGRESS_COMPLETED
            else -> ProjectWriteProgressState.STATE_IDLE
        }
    }

    fun setProjectPeriodProgress(progressState: ProjectWriteProgressState) {
        savedStateHandle.set(KEY_PROJECT_WRITE_PERIOD_PROGRESS_STATE, progressState)
    }

    fun setProjectLocationType(locationType: ProjectWriteLocationType) {
        savedStateHandle.set(KEY_PROJECT_WRITE_LOCATION_TYPE, locationType)

        if (projectLocationType.value == ProjectWriteLocationType.TYPE_ONLINE) {
            savedStateHandle.set(KEY_PROJECT_WRITE_LOCATION, null)
        }
    }

    fun setProjectLocation(projectInfo: ProjectInfo) {
        if (projectLocationType.value == ProjectWriteLocationType.TYPE_ONLINE) {
            savedStateHandle.set(KEY_PROJECT_WRITE_LOCATION, null)
        } else {
            savedStateHandle.set(KEY_PROJECT_WRITE_LOCATION, if (projectLocation.value?.key == projectInfo.key) null else projectInfo)
        }
    }

    companion object {
        private const val KEY_PROJECT_WRITE_STEP = "project_write_step"

        private const val KEY_PROJECT_WRITE_TITLE = "project_write_title"
        private const val KEY_PROJECT_WRITE_TITLE_EDIT_TEXT_STATE = "project_write_title_edit_text_state"

        private const val KEY_PROJECT_WRITE_PERIOD_PROGRESS_STATE = "project_write_period_progress_state"
        private const val KEY_PROJECT_WRITE_LOCATION_TYPE = "project_write_location_type"
        private const val KEY_PROJECT_WRITE_LOCATION = "project_write_location"
    }
}
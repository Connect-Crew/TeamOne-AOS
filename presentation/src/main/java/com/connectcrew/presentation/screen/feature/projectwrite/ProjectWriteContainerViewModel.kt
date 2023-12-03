package com.connectcrew.presentation.screen.feature.projectwrite

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.project.GetProjectInfoUseCase
import com.connectcrew.presentation.screen.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ProjectWriteContainerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val projectInfoUseCase: GetProjectInfoUseCase
) : BaseViewModel() {

    val projectProgress = savedStateHandle.getStateFlow(KEY_PROJECT_WRITE_STEP, 0)

    val projectStep1State = projectProgress
        .map { getProjectState(1, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectStep2State = projectProgress
        .map { getProjectState(2, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectStep3State = projectProgress
        .map { getProjectState(3, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectStep4State = projectProgress
        .map { getProjectState(4, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    val projectStep5State = projectProgress
        .map { getProjectState(5, it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ProjectWriteProgressState.STATE_IDLE)

    fun setWriteProgress(progress: Int) {
        if (projectProgress.value == progress) return
        savedStateHandle.set(KEY_PROJECT_WRITE_STEP, progress)
    }

    private fun getProjectState(selectProgress: Int, currentProgress: Int): ProjectWriteProgressState {
        return when {
            selectProgress == currentProgress -> ProjectWriteProgressState.STATE_PROCEEDING
            currentProgress > selectProgress -> ProjectWriteProgressState.STATE_PROGRESS_COMPLETED
            else -> ProjectWriteProgressState.STATE_IDLE
        }
    }

    companion object {
        private const val KEY_PROJECT_WRITE_STEP = "project_write_step"
    }
}
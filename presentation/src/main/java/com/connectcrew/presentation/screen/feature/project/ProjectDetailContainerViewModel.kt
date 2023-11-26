package com.connectcrew.presentation.screen.feature.project

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.presentation.model.project.ProjectFeedDetail
import com.connectcrew.presentation.model.project.ProjectFeedDetailCategory
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectDetailContainerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    signViewModelDelegate: SignViewModelDelegate
) : BaseViewModel(), SignViewModelDelegate by signViewModelDelegate {

    val projectId = savedStateHandle.getStateFlow<Int?>(KEY_PROJECT_ID, null)

    private val selectedProjectDetailCategory =
        savedStateHandle.getStateFlow(KEY_PROJECT_DETAIL_CATEGORY, ProjectFeedDetailCategory.INTRODUCTION)

    private val _invalidateProjectDetail = MutableEventFlow<Unit>()
    val invalidateProjectDetail: EventFlow<Unit> = _invalidateProjectDetail

    private val _navigateToProjectEnrollDialog = MutableEventFlow<Pair<Int, ProjectFeedDetail>>()
    val navigateToProjectEnrollDialog: EventFlow<Pair<Int, ProjectFeedDetail>> = _navigateToProjectEnrollDialog

    fun setSelectedProjectDetailCategory(category: ProjectFeedDetailCategory) {
        if (selectedProjectDetailCategory.value == category) return
        savedStateHandle.set(KEY_PROJECT_DETAIL_CATEGORY, category)
    }

    fun invalidateProjectDetail() {
        viewModelScope.launch {
            _invalidateProjectDetail.emit(Unit)
        }
    }

    fun navigateToProjectEnrollDialog(projectFeedDetail: ProjectFeedDetail) {
        viewModelScope.launch {
            _navigateToProjectEnrollDialog.emit(projectId.value!! to projectFeedDetail)
        }
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_DETAIL_CATEGORY = "project_detail_category"
    }
}
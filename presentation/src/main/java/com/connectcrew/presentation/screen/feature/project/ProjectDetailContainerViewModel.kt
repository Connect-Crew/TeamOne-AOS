package com.connectcrew.presentation.screen.feature.project

import androidx.lifecycle.SavedStateHandle
import com.connectcrew.presentation.model.project.ProjectFeedDetailCategory
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectDetailContainerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    signViewModelDelegate: SignViewModelDelegate
) : BaseViewModel(), SignViewModelDelegate by signViewModelDelegate {

    val projectId = savedStateHandle.getStateFlow<Int?>(KEY_PROJECT_ID, null)

    private val selectedProjectDetailCategory =
        savedStateHandle.getStateFlow(KEY_PROJECT_DETAIL_CATEGORY, ProjectFeedDetailCategory.INTRODUCTION)

    fun setSelectedProjectDetailCategory(category: ProjectFeedDetailCategory) {
        if (selectedProjectDetailCategory.value == category) return
        savedStateHandle.set(KEY_PROJECT_DETAIL_CATEGORY, category)
    }

    companion object {
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_DETAIL_CATEGORY = "project_detail_category"
    }
}
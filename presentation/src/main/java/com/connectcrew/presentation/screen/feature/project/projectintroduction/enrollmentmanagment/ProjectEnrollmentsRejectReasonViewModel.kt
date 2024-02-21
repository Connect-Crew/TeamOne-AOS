package com.connectcrew.presentation.screen.feature.project.projectintroduction.enrollmentmanagment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProjectEnrollmentsRejectReasonViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val applyId
        get() = savedStateHandle.get<Int>(KEY_APPLY_ID) ?: -1

    val projectRejectReason = savedStateHandle.getStateFlow(KEY_PROJECT_REJECT_REASON, "")

    private val _navigateToBack = MutableEventFlow<Pair<Int, String>>()
    val navigateToBack: EventFlow<Pair<Int, String>> = _navigateToBack

    fun setRejectReason(reason: String) {
        if (projectRejectReason.value == reason) return
        savedStateHandle.set(KEY_PROJECT_REJECT_REASON, reason.trim())
    }

    fun navigateToBack() {
        viewModelScope.launch {
            _navigateToBack.emit(applyId to projectRejectReason.value)
        }
    }

    companion object {
        private const val KEY_APPLY_ID = "apply_id"
        private const val KEY_PROJECT_REJECT_REASON = "project_reject_reason"
    }
}
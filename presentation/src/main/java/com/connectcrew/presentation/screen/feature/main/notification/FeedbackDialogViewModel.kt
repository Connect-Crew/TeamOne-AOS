package com.connectcrew.presentation.screen.feature.main.notification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackDialogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _navigateToFeedbackCompletedDialog = MutableEventFlow<Unit>()
    val navigateToFeedbackCompletedDialog: EventFlow<Unit> = _navigateToFeedbackCompletedDialog

    val feedbackMessage = savedStateHandle.getStateFlow(KEY_FEEDBACK_MESSAGE, "")

    fun setFeedbackMessage(message: String) {
        if (feedbackMessage.value == message) return
        savedStateHandle.set(KEY_FEEDBACK_MESSAGE, message.trim())
    }

    fun submitFeedback() {
        viewModelScope.launch {
            //::TODO("피드백 데이터 전송 구현")
        }
    }

    companion object {
        private const val KEY_FEEDBACK_MESSAGE = "feedback_message"
    }
}
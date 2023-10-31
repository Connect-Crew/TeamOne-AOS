package com.connectcrew.presentation.screen.feature.webview

import androidx.lifecycle.SavedStateHandle
import com.connectcrew.presentation.screen.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val title = savedStateHandle.getStateFlow(KEY_TITLE, "")

    private val _progress = MutableStateFlow<Int>(0)
    val progress: StateFlow<Int> = _progress

    private val _isFinish = MutableStateFlow<Boolean>(false)
    val isFinish: StateFlow<Boolean> = _isFinish

    fun setProgress(value: Int) {
        if (progress.value == value) return
        _progress.value = value
    }

    fun setFinished(isFinish: Boolean) {
        if (this.isFinish.value == isFinish) return
        _isFinish.value = isFinish
    }

    fun setTitle(title: String) {
        savedStateHandle.set(KEY_TITLE, title)
    }

    companion object {
        private const val KEY_TITLE = "title"
    }
}
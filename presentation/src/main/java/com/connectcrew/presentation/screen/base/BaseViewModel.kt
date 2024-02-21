package com.connectcrew.presentation.screen.base

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {

    // Used to re-run flows on command
    private val refreshSignal = MutableSharedFlow<Unit>()

    // Used to run flows on init and also on command
    protected val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    private val _swipeRefreshing = MutableStateFlow(false)
    val swipeRefreshing: StateFlow<Boolean> = _swipeRefreshing

    private val _messageRes = MutableSharedFlow<Int>()
    val messageRes: SharedFlow<Int> = _messageRes

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    fun onRefresh() = viewModelScope.launch {
        refreshSignal.emit(Unit)
    }

    fun onSwipeRefresh() = viewModelScope.launch {
        isSwipeRefreshing(true)
        refreshSignal.emit(Unit)
    }

    fun isSwipeRefreshing(isRefreshing: Boolean) {
        _swipeRefreshing.value = isRefreshing
    }

    fun setMessage(@StringRes messageRes: Int) {
        viewModelScope.launch {
            _messageRes.emit(messageRes)
        }
    }

    fun setMessage(message: String) {
        viewModelScope.launch {
            _message.emit(message)
        }
    }

    enum class InitializerUiState {
        Loading,
        Success,
        Error
    }
}
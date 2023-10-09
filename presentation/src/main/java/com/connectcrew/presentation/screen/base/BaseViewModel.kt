package com.connectcrew.presentation.screen.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

open class BaseViewModel: ViewModel() {

    // Used to re-run flows on command
    private val refreshSignal = MutableSharedFlow<Unit>()

    // Used to run flows on init and also on command
    protected val loadDataSignal: Flow<Unit> = flow {
        emit(Unit)
        emitAll(refreshSignal)
    }

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    fun setLoading(isLoading: Boolean) {
        _loading.value = isLoading
    }

    fun onRefresh() = viewModelScope.launch {
        refreshSignal.emit(Unit)
    }
}
package com.connectcrew.presentation.screen

import androidx.lifecycle.SavedStateHandle
import com.connectcrew.presentation.screen.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : BaseViewModel() {

}
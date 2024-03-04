package com.connectcrew.presentation.screen.feature.main.mypage

import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.user.GetUserInfoUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.WhileViewSubscribed
import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    getUserInfoUseCase: GetUserInfoUseCase,
    signViewModelDelegate: SignViewModelDelegate
) : BaseViewModel(), SignViewModelDelegate by signViewModelDelegate {

    private val currentUserInfoResult = loadDataSignal
        .flatMapLatest { getUserInfoUseCase(Unit).asResult() }
        .stateIn(viewModelScope, SharingStarted.Lazily, ApiResult.Loading)

    val myPageUiState = currentUserInfoResult
        .mapLatest {
            when (it) {
                is ApiResult.Loading -> InitializerUiState.Loading
                is ApiResult.Success -> InitializerUiState.Success
                is ApiResult.Error -> InitializerUiState.Error
            }
        }
        .stateIn(viewModelScope, WhileViewSubscribed, InitializerUiState.Loading)
}
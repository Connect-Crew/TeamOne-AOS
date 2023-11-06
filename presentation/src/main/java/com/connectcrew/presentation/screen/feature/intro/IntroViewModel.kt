package com.connectcrew.presentation.screen.feature.intro

import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.user.ObserveUserInfoUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.model.user.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val observeUserInfoUseCase: ObserveUserInfoUseCase,
) : BaseViewModel() {

    private val _navigateToHome = MutableEventFlow<Unit>()
    val navigateToHome: EventFlow<Unit> = _navigateToHome

    private val _navigateToSignIn = MutableEventFlow<Unit>()
    val navigateToSignIn: EventFlow<Unit> = _navigateToSignIn

    private val _navigateToErrorDialog = MutableEventFlow<Unit>()
    val navigateToErrorDialog: EventFlow<Unit> = _navigateToErrorDialog

    init {
        viewModelScope.launch {
            //::TODO 유저 정보 조회 API 호출하기 (현재 백엔드 작업 중)
            observeUserInfoUseCase(Unit)
                .asResult()
                .debounce(1000)
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> {
                            if (it.data?.asItem() == null) {
                                _navigateToSignIn.emit(Unit)
                            } else {
                                _navigateToHome.emit(Unit)
                            }
                        }

                        is ApiResult.Error -> _navigateToSignIn.emit(Unit)
                    }
                }
        }
    }
}
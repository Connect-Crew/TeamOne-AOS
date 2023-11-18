package com.connectcrew.presentation.screen.feature.intro

import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.user.GetUserInfoUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.UnAuthorizedException
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.delegate.SignViewModelDelegate
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    signViewModelDelegate: SignViewModelDelegate
) : BaseViewModel(), SignViewModelDelegate by signViewModelDelegate {

    private val currentUserInfo = loadDataSignal.combine(userToken, ::Pair)
        .flatMapLatest { (_, userToken) -> getUserInfoUseCase(GetUserInfoUseCase.Params(userToken)).asResult() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ApiResult.Loading)

    private val _navigateToHome = MutableEventFlow<Unit>()
    val navigateToHome: EventFlow<Unit> = _navigateToHome

    private val _navigateToSignIn = MutableEventFlow<Unit>()
    val navigateToSignIn: EventFlow<Unit> = _navigateToSignIn

    private val _navigateToErrorDialog = MutableEventFlow<Unit>()
    val navigateToErrorDialog: EventFlow<Unit> = _navigateToErrorDialog

    init {
        viewModelScope.launch {
            currentUserInfo
                .debounce(600)
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> _navigateToHome.emit(Unit)
                        is ApiResult.Error -> when (it.exception) {
                            is IOException -> _navigateToErrorDialog.emit(Unit)
                            is TeamOneException -> when (it.exception) {
                                is UnAuthorizedException -> refreshUserToken()
                                else -> _navigateToSignIn.emit(Unit)
                            }

                            else -> _navigateToSignIn.emit(Unit)
                        }
                    }
                }
        }
    }
}
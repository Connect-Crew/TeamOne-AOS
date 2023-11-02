package com.connectcrew.presentation.screen.feature.intro

import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.sign.SignInUseCase
import com.connectcrew.domain.usecase.token.GetTokenInfoUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.NotFoundException
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.asResult
import com.connectcrew.domain.util.data
import com.connectcrew.presentation.model.token.asItem
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    private val getTokenInfoUseCase: GetTokenInfoUseCase,
    private val signInUseCase: SignInUseCase
) : BaseViewModel() {

    private val _navigateToHome = MutableEventFlow<Unit>()
    val navigateToHome: EventFlow<Unit> = _navigateToHome

    private val _navigateToSignIn = MutableEventFlow<Unit>()
    val navigateToSignIn: EventFlow<Unit> = _navigateToSignIn

    private val _navigateToErrorDialog = MutableEventFlow<Unit>()
    val navigateToErrorDialog: EventFlow<Unit> = _navigateToErrorDialog

    init {
        viewModelScope.launch {
            getTokenInfoUseCase(Unit).data
                ?.asItem()
                ?.let { tokenInfo ->
                    if (!tokenInfo.accessToken.isNullOrBlank() && !tokenInfo.socialType.isNullOrBlank()) {
                        signInUseCase(SignInUseCase.Params(tokenInfo.accessToken, tokenInfo.socialType))
                            .asResult()
                            .collect { apiResult ->
                                when (apiResult) {
                                    is ApiResult.Loading -> return@collect
                                    is ApiResult.Success -> _navigateToHome.emit(Unit)
                                    is ApiResult.Error -> when (apiResult.exception) {
                                        is TeamOneException -> when (apiResult.exception) {
                                            //TODO:: UnAuthorizedException Exception 시 토큰 갱신
                                            is NotFoundException -> _navigateToSignIn.emit(Unit)
                                            else -> _navigateToErrorDialog.emit(Unit)
                                        }

                                        else -> _navigateToErrorDialog.emit(Unit)
                                    }
                                }
                            }
                    } else {
                        _navigateToSignIn.emit(Unit)
                    }
                }
        }
    }
}
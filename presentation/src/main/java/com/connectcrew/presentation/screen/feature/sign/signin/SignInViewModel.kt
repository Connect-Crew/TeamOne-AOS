package com.connectcrew.presentation.screen.feature.sign.signin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.sign.SignInUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.UnAuthorizedException
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.R
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val signInUseCase: SignInUseCase
) : BaseViewModel() {

    private val _navigateToSignInForKakao = MutableEventFlow<Unit>()
    val navigateToSignInForKakao: EventFlow<Unit> = _navigateToSignInForKakao

    private val _navigateToSignInForGoogle = MutableEventFlow<Unit>()
    val navigateToSignInForGoogle: EventFlow<Unit> = _navigateToSignInForGoogle

    private val _navigateToSignUp = MutableEventFlow<Unit>()
    val navigateToSignUp: EventFlow<Unit> = _navigateToSignUp

    private val _navigateToHome = MutableEventFlow<Unit>()
    val navigateToHome: EventFlow<Unit> = _navigateToHome

    fun validateWithOAuthLogin(token: String, oAuthType: String) {
        viewModelScope.launch {
            signInUseCase(SignInUseCase.Params(token, oAuthType))
                .asResult()
                .onEach { setLoading(it is ApiResult.Loading) }
                .collect { apiResult ->
                    when (apiResult) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> _navigateToHome.emit(Unit)
                        is ApiResult.Error -> {
                            when (apiResult.exception) {
                                is IOException -> setMessage(R.string.unknown_error)
                                is TeamOneException -> when (apiResult.exception) {
                                    is UnAuthorizedException -> _navigateToSignUp.emit(Unit)
                                    else -> setMessage(apiResult.exception.message.toString())
                                }

                                else -> setMessage(R.string.unknown_error)
                            }
                        }
                    }
                }
        }
    }

    fun navigateToSignInForKakao() {
        viewModelScope.launch {
            _navigateToSignInForKakao.emit(Unit)
        }
    }

    fun navigateToSignInForGoogle() {
        viewModelScope.launch {
            _navigateToSignInForGoogle.emit(Unit)
        }
    }
}
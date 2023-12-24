package com.connectcrew.presentation.screen.feature.sign.signin

import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.sign.GetAccessTokenForGoogle
import com.connectcrew.domain.usecase.sign.SignInUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.NotFoundException
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.token.TokenInfo
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import com.connectcrew.presentation.util.firebase.FirebaseUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val getAccessTokenForGoogle: GetAccessTokenForGoogle,
    private val firebaseUtil: FirebaseUtil
) : BaseViewModel() {

    private val _navigateToSignInForKakao = MutableEventFlow<Unit>()
    val navigateToSignInForKakao: EventFlow<Unit> = _navigateToSignInForKakao

    private val _navigateToSignInForGoogle = MutableEventFlow<Unit>()
    val navigateToSignInForGoogle: EventFlow<Unit> = _navigateToSignInForGoogle

    private val _navigateToSignUp = MutableEventFlow<Triple<TokenInfo, String?, String?>>()
    val navigateToSignUp: EventFlow<Triple<TokenInfo, String?, String?>> = _navigateToSignUp

    private val _navigateToHome = MutableEventFlow<Unit>()
    val navigateToHome: EventFlow<Unit> = _navigateToHome

    fun getAccessTokenForGoogle(
        authCode: String,
        oAuthType: String,
        profileUrl: String?,
        email: String?,
    ) {
        viewModelScope.launch {
            getAccessTokenForGoogle(GetAccessTokenForGoogle.Params(authCode))
                .asResult()
                .onEach { setLoading(it is ApiResult.Loading) }
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> validateWithOAuthLogin(it.data, oAuthType, profileUrl, email)
                        is ApiResult.Error -> setMessage(R.string.network_error)
                    }
                }
        }
    }

    fun validateWithOAuthLogin(
        token: String,
        oAuthType: String,
        profileUrl: String?,
        email: String?
    ) {
        viewModelScope.launch {
            signInUseCase(SignInUseCase.Params(token, firebaseUtil.getFirebaseMessageToken(), oAuthType))
                .asResult()
                .onEach { setLoading(it is ApiResult.Loading) }
                .collect { apiResult ->
                    when (apiResult) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> _navigateToHome.emit(Unit)
                        is ApiResult.Error -> {
                            when (apiResult.exception) {
                                is IOException -> setMessage(R.string.network_error)
                                is TeamOneException -> when (apiResult.exception) {
                                    is NotFoundException -> _navigateToSignUp.emit(Triple(TokenInfo(token, oAuthType), email, profileUrl))
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
            setLoading(true)
            _navigateToSignInForGoogle.emit(Unit)
        }
    }
}
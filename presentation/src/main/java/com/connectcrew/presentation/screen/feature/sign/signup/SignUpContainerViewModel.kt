package com.connectcrew.presentation.screen.feature.sign.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connectcrew.domain.usecase.sign.SignUpUseCase
import com.connectcrew.domain.util.ApiResult
import com.connectcrew.domain.util.BadRequestException
import com.connectcrew.domain.util.TeamOneException
import com.connectcrew.domain.util.asResult
import com.connectcrew.presentation.R
import com.connectcrew.presentation.model.token.TokenInfo
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.EditTextState
import com.connectcrew.presentation.util.RegexUtil
import com.connectcrew.presentation.util.Success
import com.connectcrew.presentation.util.event.EventFlow
import com.connectcrew.presentation.util.event.MutableEventFlow
import com.connectcrew.presentation.util.firebase.FirebaseUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class SignUpContainerViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val signUpUseCase: SignUpUseCase,
    private val firebaseUtil: FirebaseUtil
) : BaseViewModel() {

    private val signTokenInfo
        get() = savedStateHandle.get<TokenInfo>(KEY_SIGN_TOKEN_INFO)
    private val signEmail
        get() = savedStateHandle.get<String?>(KEY_SIGN_EMAIL)
    private val signProfileUrl
        get() = savedStateHandle.get<String?>(KEY_SIGN_PROFILE_URL)

    val isTermsOfUseForService = savedStateHandle.getStateFlow(KEY_IS_TERMS_OF_USE_FOR_SERVICE, false)
    val isTermsOfUseForPrivacy = savedStateHandle.getStateFlow(KEY_IS_TERMS_OF_USE_FOR_PRIVACY, false)

    val enableTermsOfUse: StateFlow<Boolean> = combine(
        isTermsOfUseForService,
        isTermsOfUseForPrivacy,
    ) { service, privacy ->
        service && privacy
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val userNickName
        get() = savedStateHandle.get<String>(KEY_USER_NICKNAME) ?: ""
    val userNickNameEditTextState: StateFlow<EditTextState> = savedStateHandle.getStateFlow<EditTextState>(KEY_USER_NICKNAME_STATE, EditTextState.Loading)
    val enableUserNickName = userNickNameEditTextState
        .map { it.Success }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _navigateToTermsOfUseDetail = MutableEventFlow<String>()
    val navigateToTermsOfUseDetail: EventFlow<String> = _navigateToTermsOfUseDetail

    private val _navigateToWriteUserName = MutableEventFlow<Unit>()
    val navigateToWriteUserName: EventFlow<Unit> = _navigateToWriteUserName

    private val _navigateToOnboarding = MutableEventFlow<Unit>()
    val navigateToOnboarding: EventFlow<Unit> = _navigateToOnboarding

    private val _navigateToHome = MutableEventFlow<Unit>()
    val navigateToHome: EventFlow<Unit> = _navigateToHome

    fun setTermsOfUseAllAgree(isChecked: Boolean) {
        setTermsOfUseForService(isChecked)
        setTermsOfUseForPrivacy(isChecked)
    }

    fun setTermsOfUseForService(isChecked: Boolean) {
        savedStateHandle.set(KEY_IS_TERMS_OF_USE_FOR_SERVICE, isChecked)
    }

    fun setTermsOfUseForPrivacy(isChecked: Boolean) {
        savedStateHandle.set(KEY_IS_TERMS_OF_USE_FOR_PRIVACY, isChecked)
    }

    fun setUserName(name: String) {
        if (userNickName == name) return
        savedStateHandle.set(KEY_USER_NICKNAME, name)

        if (userNickName.isEmpty()) {
            setInputUserNicknameTextState(EditTextState.Loading)
        } else if (userNickName.length < 2) {
            setInputUserNicknameTextState(EditTextState.Error(R.string.write_user_name_nickname_hint))
        } else {
            Pattern.matches(RegexUtil.PATTERN_NICKNAME_VERIFICATION, userNickName)
                .let { matches ->
                    if (matches) {
                        setInputUserNicknameTextState(EditTextState.Success)
                    } else {
                        setInputUserNicknameTextState(EditTextState.Error(R.string.write_user_name_nickname_pattern_error))
                    }
                }
        }
    }

    private fun setInputUserNicknameTextState(editTextState: EditTextState) {
        if (this.userNickNameEditTextState.value == editTextState) return
        savedStateHandle.set(KEY_USER_NICKNAME_STATE, editTextState)
    }

    fun navigateToTermsOfUseDetail(uri: String) {
        viewModelScope.launch {
            _navigateToTermsOfUseDetail.emit(uri)
        }
    }

    fun navigateToWriteUserName() {
        viewModelScope.launch {
            _navigateToWriteUserName.emit(Unit)
        }
    }

    fun navigateToOnboarding() {
        viewModelScope.launch {
            signUpUseCase(
                SignUpUseCase.Params(
                    accessToken = signTokenInfo?.accessToken ?: "",
                    fcmToken = firebaseUtil.getFirebaseMessageToken(),
                    socialType = signTokenInfo?.socialType ?: "",
                    nickname = userNickName,
                    email = signEmail,
                    profileUrl = signProfileUrl
                )
            ).asResult()
                .onEach { setLoading(it is ApiResult.Loading) }
                .collect {
                    when (it) {
                        is ApiResult.Loading -> return@collect
                        is ApiResult.Success -> _navigateToOnboarding.emit(Unit)
                        is ApiResult.Error -> when (it.exception) {
                            is TeamOneException -> when (it.exception) {
                                is BadRequestException -> setInputUserNicknameTextState(EditTextState.Error(R.string.write_user_name_nickname_duplicated_error))
                                else -> setInputUserNicknameTextState(EditTextState.ErrorMessage(it.exception.message.toString()))
                            }

                            else -> setMessage(R.string.unknown_error)
                        }
                    }
                }
        }
    }

    fun navigateToHome() {
        viewModelScope.launch {
            _navigateToHome.emit(Unit)
        }
    }

    companion object {
        private const val KEY_SIGN_TOKEN_INFO = "sign_token_info"
        private const val KEY_SIGN_EMAIL = "sign_email"
        private const val KEY_SIGN_PROFILE_URL = "sign_profile_url"

        private const val KEY_IS_TERMS_OF_USE_FOR_SERVICE = "is_terms_of_use_for_service"
        private const val KEY_IS_TERMS_OF_USE_FOR_PRIVACY = "is_terms_of_use_for_privacy"

        private const val KEY_USER_NICKNAME = "user_nickname"
        private const val KEY_USER_NICKNAME_STATE = "user_nickname_state"
    }
}
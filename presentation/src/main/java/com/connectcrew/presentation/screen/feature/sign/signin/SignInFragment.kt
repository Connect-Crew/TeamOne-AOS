package com.connectcrew.presentation.screen.feature.sign.signin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentSignInBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val signInViewModel: SignInViewModel by viewModels()

    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, exception ->
        when {
            exception != null -> when {
                (exception is AuthError && exception.response.error == IOException) -> signInViewModel.setMessage(R.string.network_error)
                else -> signInViewModel.setMessage(R.string.unknown_error)
            }

            token != null -> UserApiClient.instance.me { user, _ ->
                user?.let { signInViewModel.validateWithOAuthLogin(token.accessToken, SOCIAL_TYPE_KAKAO) }
                    ?: signInViewModel.setMessage(R.string.sign_in_kakao_fail)
            }

            else -> signInViewModel.setMessage(R.string.unknown_error)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = signInViewModel
        }

        initObserver()
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                signInViewModel.navigateToSignInForKakao.collect {
                    // 카카오톡이 설치되어 있으면 카카오톡 로그인. 아니면 카카오계정으로 로그인
                    if (UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
                        UserApiClient.instance.loginWithKakaoTalk(requireContext(), callback = kakaoCallback)
                    } else {
                        UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = kakaoCallback)
                    }
                }
            }

            launch {
                signInViewModel.navigateToSignInForGoogle.collect {
                    //TODO:: 구글 로그인 환경설정 후 build 설정
                }
            }

            launch {
                signInViewModel.navigateToSignUp.collect {
                    //TODO:: 회원가입 화면으로 이동
                }
            }

            launch {
                signInViewModel.navigateToHome.collect {
                    activity?.run {
                        findNavController().safeNavigate(SignInFragmentDirections.actionSignInFragmentToActivityMain())
                        finish()
                    }
                }
            }

            launch {
                signInViewModel.loading.collect {
                    if (it) showLoadingDialog() else hideLoadingDialog()
                }
            }

            launch {
                signInViewModel.messageRes.collect {
                    showToast(it)
                }
            }

            launch {
                signInViewModel.message.collect {
                    showToast(it)
                }
            }
        }
    }

    companion object {
        private const val IOException = "ProtocolError"
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
        private const val SOCIAL_TYPE_GOOGLE = "GOOGLE"
    }
}
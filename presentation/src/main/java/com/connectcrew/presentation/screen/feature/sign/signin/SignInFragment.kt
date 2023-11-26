package com.connectcrew.presentation.screen.feature.sign.signin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.BuildConfig
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentSignInBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.google.android.gms.tasks.Task
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(R.layout.fragment_sign_in) {

    private val signInViewModel: SignInViewModel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var loginLauncher: ActivityResultLauncher<Intent>

    private val kakaoCallback: (OAuthToken?, Throwable?) -> Unit = { token, exception ->
        when {
            exception != null -> when {
                (exception is AuthError && exception.response.error == IOException) -> signInViewModel.setMessage(R.string.network_error)
                else -> signInViewModel.setMessage(R.string.unknown_error)
            }

            token != null -> UserApiClient.instance.me { user, _ ->
                user?.let {
                    signInViewModel.validateWithOAuthLogin(
                        token = token.accessToken,
                        oAuthType = SOCIAL_TYPE_KAKAO,
                        profileUrl = it.kakaoAccount?.profile?.profileImageUrl,
                        email = it.kakaoAccount?.email
                    )
                } ?: signInViewModel.setMessage(R.string.sign_in_kakao_fail)
            }

            else -> signInViewModel.setMessage(R.string.sign_in_kakao_fail)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = signInViewModel
        }

        initObserver()
        initGoogle()
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
                    loginLauncher.launch(googleSignInClient.signInIntent)
                }
            }

            launch {
                signInViewModel.navigateToSignUp.collect { (tokenInfo, email, profileUrl) ->
                    findNavController().safeNavigate(SignInFragmentDirections.actionSignInFragmentToNavSignUp(tokenInfo, email, profileUrl))
                }
            }

            launch {
                signInViewModel.navigateToHome.collect {
                    activity?.run {
                        findNavController().safeNavigate(SignInFragmentDirections.actionNavSignInToActivityMain())
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

    private fun initGoogle() {
        val googleClientId = BuildConfig.GOOGLE_CLIENT_ID
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.DRIVE_APPFOLDER))
            .requestIdToken(googleClientId)
            .requestServerAuthCode(googleClientId)
            .requestEmail()
            .requestProfile()
            .build()

        loginLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            signInViewModel.setLoading(false)

            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleSignInResult(task)
            } else {
                signInViewModel.setMessage(R.string.sign_in_google_fail)
            }
        }

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val signInResult = completedTask.getResult(ApiException::class.java)
            signInResult.serverAuthCode
                ?.let {
                    signInViewModel.getAccessTokenForGoogle(
                        authCode = it,
                        oAuthType = SOCIAL_TYPE_GOOGLE,
                        profileUrl = signInResult.photoUrl.toString(),
                        email = signInResult.email
                    )
                }
                ?: signInViewModel.setMessage(R.string.sign_in_google_fail)
        } catch (e: ApiException) {
            signInViewModel.setMessage(R.string.unknown_error)
        }
    }

    companion object {
        private const val IOException = "ProtocolError"
        private const val SOCIAL_TYPE_KAKAO = "KAKAO"
        private const val SOCIAL_TYPE_GOOGLE = "GOOGLE"
    }
}
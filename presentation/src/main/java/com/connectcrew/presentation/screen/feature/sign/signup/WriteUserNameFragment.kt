package com.connectcrew.presentation.screen.feature.sign.signup

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentWriteUserNameBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.setTextInputError
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WriteUserNameFragment : BaseFragment<FragmentWriteUserNameBinding>(R.layout.fragment_write_user_name) {

    private val signUpContainerViewModel: SignUpContainerViewModel by hiltNavGraphViewModels(R.id.nav_sign_up)

    private val userSignUpNicknameTextChangListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = signUpContainerViewModel.viewModelScope,
            onDebounceEditTextChange = signUpContainerViewModel::setUserName
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = signUpContainerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
        with(dataBinding) {
            tlSignUp.setNavigationOnClickListener {
                if (!findNavController().navigateUp()) {
                    requireActivity().finish()
                }
            }

            tietUserName.addTextChangedListener(userSignUpNicknameTextChangListener)
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                signUpContainerViewModel.userNickNameEditTextState.collect {
                    dataBinding.tilUserName.setTextInputError(it)
                }
            }

            launch {
                signUpContainerViewModel.navigateToOnboarding.collect {
                    findNavController().safeNavigate(WriteUserNameFragmentDirections.actionWriteUserNameFragmentToOnBoardingFragment())
                }
            }


            launch {
                signUpContainerViewModel.loading.collect {
                    if (it) showLoadingDialog() else hideLoadingDialog()
                }
            }

            launch {
                signUpContainerViewModel.message.collect {
                    showToast(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        dataBinding.tietUserName.removeTextChangedListener(userSignUpNicknameTextChangListener)
        super.onDestroyView()
    }
}
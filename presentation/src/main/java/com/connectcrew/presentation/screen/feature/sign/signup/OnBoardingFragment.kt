package com.connectcrew.presentation.screen.feature.sign.signup

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentOnBoardingBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.sign.signin.SignInFragmentDirections
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingFragment : BaseFragment<FragmentOnBoardingBinding>(R.layout.fragment_on_boarding) {

    private val signUpContainerViewModel: SignUpContainerViewModel by hiltNavGraphViewModels(R.id.nav_sign_up)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = signUpContainerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initObserver()
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            signUpContainerViewModel.navigateToHome.collect {
                activity?.run {
                    findNavController().safeNavigate(SignInFragmentDirections.actionNavSignInToActivityMain())
                    finish()
                }
            }
        }
    }
}
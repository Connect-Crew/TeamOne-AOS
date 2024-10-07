package com.connectcrew.presentation.screen.feature.sign.signup

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentTermsOfUseBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermsOfUseFragment : BaseFragment<FragmentTermsOfUseBinding>(R.layout.fragment_terms_of_use) {

    private val signUpContainerViewModel: SignUpContainerViewModel by hiltNavGraphViewModels(R.id.nav_sign_up)

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
        dataBinding.tlTermsOfUse.apply {
            setNavigationOnClickListener {
                if (!findNavController().navigateUp()) {
                    requireActivity().finish()
                }
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                signUpContainerViewModel.navigateToTermsOfUseDetail.collect {
                    findNavController().safeNavigate(TermsOfUseFragmentDirections.actionTermsOfUseFragmentToWebViewFragment(it))
                }
            }

            launch {
                signUpContainerViewModel.navigateToWriteUserName.collect {
                    findNavController().safeNavigate(TermsOfUseFragmentDirections.actionTermsOfUseFragmentToWriteUserNameFragment())
                }
            }
        }
    }
}
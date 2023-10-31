package com.connectcrew.presentation.screen.feature.intro

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentIntroBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IntroFragment : BaseFragment<FragmentIntroBinding>(R.layout.fragment_intro) {

    private val introViewModel: IntroViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObserver()
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                introViewModel.navigateToSignIn.collect {
                    findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToNavSignIn())
                }
            }

            launch {
                introViewModel.navigateToHome.collect {
                    activity?.run {
                        findNavController().safeNavigate(IntroFragmentDirections.actionIntroFragmentToActivityMain())
                        finish()
                    }
                }
            }

            launch {
                introViewModel.navigateToErrorDialog.collect {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle(resources.getString(R.string.network_error_title))
                        .setMessage(resources.getString(R.string.network_error))
                        .setCancelable(false)
                        .setNegativeButton(resources.getString(R.string.common_exit)) { dialog, _: Int ->
                            dialog.dismiss()
                            requireActivity().finish()
                        }.show()
                }
            }
        }
    }
}
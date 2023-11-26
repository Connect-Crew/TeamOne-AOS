package com.connectcrew.presentation.screen.feature.intro

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            introViewModel.navigateToNextScreen()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        hideSystemUi()
    }

    private fun initListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPostNotificationPermission()
        } else {
            introViewModel.navigateToNextScreen()
        }
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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestPostNotificationPermission() {
        val isPermissionGranted = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
        val isShowPermissionRequest = shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
        when {
            isPermissionGranted == PackageManager.PERMISSION_GRANTED || isShowPermissionRequest -> introViewModel.navigateToNextScreen()
            else -> requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, false)
        WindowInsetsControllerCompat(requireActivity().window, dataBinding.root).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(requireActivity().window, true)
        WindowInsetsControllerCompat(requireActivity().window, dataBinding.root).show(WindowInsetsCompat.Type.systemBars())
    }

    override fun onDestroyView() {
        showSystemUi()
        super.onDestroyView()
    }
}
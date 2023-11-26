package com.connectcrew.presentation.screen.feature.project.projectintroduction

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectEnrollmentReasonBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.util.hideKeyboard
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectEnrollmentReasonAlertDialog : BaseAlertDialogFragment<DialogProjectEnrollmentReasonBinding>() {

    override val layoutResId = R.layout.dialog_project_enrollment_reason

    private val projectEnrollmentViewModel: ProjectEnrollmentViewModel by hiltNavGraphViewModels(R.id.nav_project_enrollment)

    private val projectEnrollReasonTextChangeListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = projectEnrollmentViewModel.viewModelScope,
            onDebounceEditTextChange = projectEnrollmentViewModel::setEnrollmentReason
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectEnrollmentViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        isCancelable = false
        projectEnrollmentViewModel.setEnrollmentReason("")
    }

    private fun initListener() {
        with(dataBinding) {
            btnCancel.setOnSingleClickListener {
                lifecycleScope.launch {
                    dataBinding.tietWriteEnrollmentReason.hideKeyboard().also {
                        delay(100)
                        dismiss()
                    }
                }
            }

            btnEnrollment.setOnSingleClickListener {
                projectEnrollmentViewModel.setProjectEnrollment()
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectEnrollmentViewModel.navigateToProjectEnrollmentCompleted.collect {
                    dataBinding.tietWriteEnrollmentReason.hideKeyboard().also {
                        delay(100)
                        findNavController().safeNavigate(ProjectEnrollmentReasonAlertDialogDirections.actionProjectEnrollmentReasonAlertDialogToProjectEnrollmentCompletedAlertDialog())
                    }
                }
            }

            launch {
                projectEnrollmentViewModel.loading.collect {
                    if (it) showProgress() else hideProgress()
                }
            }

            launch {
                projectEnrollmentViewModel.messageRes.collect {
                    showToast(it)
                }
            }

            launch {
                projectEnrollmentViewModel.message.collect {
                    showToast(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dataBinding.tietWriteEnrollmentReason.addTextChangedListener(projectEnrollReasonTextChangeListener)
    }

    override fun onPause() {
        super.onPause()
        dataBinding.tietWriteEnrollmentReason.removeTextChangedListener(projectEnrollReasonTextChangeListener)
    }
}
package com.connectcrew.presentation.screen.feature.project.projectintroduction.enrollmentmanagment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectEnrollmentsRejectReasonBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.util.Const.KEY_PROJECT_ENROLLMENT_REJECT_REASON
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectEnrollmentsRejectReasonDialog : BaseAlertDialogFragment<DialogProjectEnrollmentsRejectReasonBinding>() {

    override val layoutResId = R.layout.dialog_project_enrollments_reject_reason

    private val projectEnrollmentsRejectReasonViewModel: ProjectEnrollmentsRejectReasonViewModel by viewModels()

    private val projectEnrollmentsRejectReasonTextChangeListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = projectEnrollmentsRejectReasonViewModel.viewModelScope,
            onDebounceEditTextChange = projectEnrollmentsRejectReasonViewModel::setRejectReason
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectEnrollmentsRejectReasonViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
        dataBinding.btnCancel.setOnSingleClickListener { dismiss() }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            projectEnrollmentsRejectReasonViewModel.navigateToBack.collect {
                findNavController().run {
                    previousBackStackEntry?.savedStateHandle?.apply {
                        set(KEY_PROJECT_ENROLLMENT_REJECT_REASON, it)
                        navigateUp()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dataBinding.tietWriteRejectReason.addTextChangedListener(projectEnrollmentsRejectReasonTextChangeListener)
    }

    override fun onPause() {
        super.onPause()
        dataBinding.tietWriteRejectReason.removeTextChangedListener(projectEnrollmentsRejectReasonTextChangeListener)
    }
}
package com.connectcrew.presentation.screen.feature.project.projectreport

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectReportReasonBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.screen.feature.project.ProjectDetailContainerViewModel
import com.connectcrew.presentation.util.hideKeyboard
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.view.createAlert
import com.connectcrew.presentation.util.view.dialogViewBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectReportReasonAlertDialog : BaseAlertDialogFragment<DialogProjectReportReasonBinding>() {

    override val layoutResId = R.layout.dialog_project_report_reason

    private val projectDetailContainerViewModel: ProjectDetailContainerViewModel by hiltNavGraphViewModels(R.id.nav_project_detail)

    private val projectEnrollReasonTextChangeListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = projectDetailContainerViewModel.viewModelScope,
            onDebounceEditTextChange = projectDetailContainerViewModel::setProjectReportReason
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectDetailContainerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
        dataBinding.btnCancel.setOnSingleClickListener {
            lifecycleScope.launch {
                dataBinding.tietWriteProjectReportReason.hideKeyboard().also {
                    delay(100)
                    dismiss()
                }
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectDetailContainerViewModel.navigateToProjectReportCompletedDialog.collect {
                    dataBinding.tietWriteProjectReportReason.hideKeyboard().also {
                        delay(100)
                        createAlert(requireContext())
                            .dialogViewBuilder(
                                titleRes = R.string.project_detail_report_completed,
                                descriptionRes = R.string.project_detail_report_completed_description,
                                iconDrawableRes = R.drawable.ic_siren,
                                isNegativeButtonVisible = false,
                                onClickPositiveButton = { dismiss() }
                            )
                            .show()
                    }
                }
            }

            launch {
                projectDetailContainerViewModel.loading.collect {
                    if (it) showLoadingDialog() else hideLoadingDialog()
                }
            }

            launch {
                projectDetailContainerViewModel.message.collect {
                    showToast(it)
                }
            }

            launch {
                projectDetailContainerViewModel.messageRes.collect {
                    showToast(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dataBinding.tietWriteProjectReportReason.addTextChangedListener(projectEnrollReasonTextChangeListener)
    }

    override fun onPause() {
        super.onPause()
        dataBinding.tietWriteProjectReportReason.removeTextChangedListener(projectEnrollReasonTextChangeListener)
    }
}
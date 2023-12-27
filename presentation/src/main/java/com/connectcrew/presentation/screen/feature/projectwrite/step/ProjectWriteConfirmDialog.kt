package com.connectcrew.presentation.screen.feature.projectwrite.step

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectWriteConfirmBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.screen.feature.projectwrite.ProjectWriteContainerViewModel
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectWriteConfirmDialog : BaseAlertDialogFragment<DialogProjectWriteConfirmBinding>() {

    override val layoutResId = R.layout.dialog_project_write_confirm

    private val projectWriteContainerViewModel: ProjectWriteContainerViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        with(dataBinding) {
            btnCancel.setOnSingleClickListener {
                dismiss()
            }

            btnConfirm.setOnSingleClickListener {
                dismiss()
                projectWriteContainerViewModel.navigateToProjectDetail()
            }
        }
    }
}
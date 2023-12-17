package com.connectcrew.presentation.screen.feature.projectwrite.step

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectWriteExitBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.screen.feature.projectwrite.ProjectWriteContainerViewModel
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectWriteExitDialog : BaseAlertDialogFragment<DialogProjectWriteExitBinding>() {

    override val layoutResId = R.layout.dialog_project_write_exit

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
                projectWriteContainerViewModel.navigateToExit()
            }
        }
    }
}


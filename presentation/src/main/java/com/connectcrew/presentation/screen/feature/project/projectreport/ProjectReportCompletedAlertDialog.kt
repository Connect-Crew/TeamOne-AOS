package com.connectcrew.presentation.screen.feature.project.projectreport

import android.os.Bundle
import android.view.View
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectReportCompletedBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectReportCompletedAlertDialog : BaseAlertDialogFragment<DialogProjectReportCompletedBinding>() {

    override val layoutResId = R.layout.dialog_project_report_completed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        dataBinding.btnCompleted.setOnSingleClickListener { dismiss() }
    }
}
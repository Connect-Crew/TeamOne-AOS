package com.connectcrew.presentation.screen.feature.project.projectintroduction

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectEnrollmentCompletedBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.util.Const.KEY_IS_PROJECT_UPDATE
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectEnrollmentCompletedAlertDialog : BaseAlertDialogFragment<DialogProjectEnrollmentCompletedBinding>() {

    override val layoutResId: Int = R.layout.dialog_project_enrollment_completed

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false
        initListener()
    }

    private fun initListener() {
        dataBinding.btnCompleted.setOnSingleClickListener {
            findNavController().run {
                getBackStackEntry(R.id.projectDetailContainerFragment).savedStateHandle.apply {
                    set(KEY_IS_PROJECT_UPDATE, true)
                }
                navigateUp()
            }
        }
    }
}
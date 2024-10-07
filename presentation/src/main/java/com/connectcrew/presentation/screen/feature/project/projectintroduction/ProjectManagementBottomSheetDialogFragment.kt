package com.connectcrew.presentation.screen.feature.project.projectintroduction

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectManagementBinding
import com.connectcrew.presentation.screen.base.BaseBottomSheetFragment
import com.connectcrew.presentation.screen.feature.project.ProjectDetailContainerViewModel
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectManagementBottomSheetDialogFragment : BaseBottomSheetFragment<DialogProjectManagementBinding>() {

    override val layoutResId = R.layout.dialog_project_management

    override val backgroundColor: Int = R.color.color_ffffff

    private val projectDetailContainerViewModel: ProjectDetailContainerViewModel by hiltNavGraphViewModels(R.id.nav_project_detail)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectDetailContainerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initListener()
    }

    private fun initListener() {
        dataBinding.btnCancel.setOnSingleClickListener {
            dismiss()
        }
    }
}
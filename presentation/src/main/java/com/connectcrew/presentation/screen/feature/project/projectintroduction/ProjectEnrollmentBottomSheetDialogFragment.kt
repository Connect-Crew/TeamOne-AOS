package com.connectcrew.presentation.screen.feature.project.projectintroduction

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.project.enrollment.ProjectEnrollmentAdapter
import com.connectcrew.presentation.databinding.DialogProjectEnrollmentBinding
import com.connectcrew.presentation.screen.base.BaseBottomSheetFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectEnrollmentBottomSheetDialogFragment : BaseBottomSheetFragment<DialogProjectEnrollmentBinding>() {

    override val layoutResId: Int = R.layout.dialog_project_enrollment

    override val backgroundColor: Int = android.R.color.white

    private val projectEnrollmentViewModel: ProjectEnrollmentViewModel by hiltNavGraphViewModels(R.id.nav_project_enrollment)

    private val projectEnrollmentAdapter by lazy {
        ProjectEnrollmentAdapter(projectEnrollmentViewModel::navigateToProjectEnrollmentReason)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initObserver()
    }

    private fun initView() {
        with(dataBinding) {
            btnCancel.setOnSingleClickListener { findNavController().popBackStack() }

            rvProjectEnroll.apply {
                adapter = projectEnrollmentAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                addItemDecoration(RecyclerviewItemDecoration(0, 12, 0, 0, R.layout.item_project_detail_introduction_enrollment))
            }
        }
    }

    private fun initObserver() {
        // 지원하기
        launchAndRepeatWithViewLifecycle {
            launch {
                projectEnrollmentViewModel.projectFeedDetail.filterNotNull().collect {
                    projectEnrollmentAdapter.submitList(it.recruitStatus)
                }
            }

            launch {
                projectEnrollmentViewModel.navigateToProjectEnrollmentReason.collect {
                    findNavController().safeNavigate(ProjectEnrollmentBottomSheetDialogFragmentDirections.actionProjectEnrollmentBottomSheetDialogFragmentToProjectEnrollmentReasonAlertDialog())
                }
            }
        }
    }

    override fun onDestroyView() {
        dataBinding.rvProjectEnroll.adapter = null
        super.onDestroyView()
    }
}
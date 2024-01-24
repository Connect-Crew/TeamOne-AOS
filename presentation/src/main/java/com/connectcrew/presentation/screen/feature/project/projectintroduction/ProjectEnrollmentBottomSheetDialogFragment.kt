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
import com.connectcrew.presentation.util.Const
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.view.createAlert
import com.connectcrew.presentation.util.view.dialogViewBuilder
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectEnrollmentBottomSheetDialogFragment : BaseBottomSheetFragment<DialogProjectEnrollmentBinding>() {

    override val layoutResId: Int = R.layout.dialog_project_enrollment

    override val backgroundColor: Int = R.color.color_ffffff

    private val projectEnrollmentViewModel: ProjectEnrollmentViewModel by hiltNavGraphViewModels(R.id.nav_project_enrollment)

    private val projectEnrollmentAdapter by lazy {
        ProjectEnrollmentAdapter(projectEnrollmentViewModel::navigateToProjectEnrollmentReasonDialog)
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
        launchAndRepeatWithViewLifecycle {
            launch {
                projectEnrollmentViewModel.projectFeedDetail.filterNotNull().collect {
                    projectEnrollmentAdapter.submitList(it.recruitStatus)
                }
            }

            launch {
                projectEnrollmentViewModel.navigateToProjectEnrollmentReasonDialog.collect {
                    findNavController().safeNavigate(ProjectEnrollmentBottomSheetDialogFragmentDirections.actionProjectEnrollmentBottomSheetDialogFragmentToProjectEnrollmentReasonAlertDialog())
                }
            }

            launch {
                projectEnrollmentViewModel.navigateToProjectEnrollmentCompletedDialog.collect {
                    createAlert(requireContext(), false)
                        .dialogViewBuilder(
                            titleRes = R.string.project_detail_enrollment_completed,
                            descriptionRes = R.string.project_detail_enrollment_completed_description,
                            isNegativeButtonVisible = false,
                            onClickPositiveButton = {
                                findNavController().run {
                                    getBackStackEntry(R.id.projectDetailContainerFragment).savedStateHandle.apply {
                                        set(Const.KEY_IS_PROJECT_UPDATE, true)
                                    }
                                    navigateUp()
                                }
                            }
                        )
                        .show()
                }
            }

            launch {
                projectEnrollmentViewModel.loading.collect {
                    if (it) showLoadingDialog() else hideLoadingDialog()
                }
            }
        }
    }

    override fun onDestroyView() {
        dataBinding.rvProjectEnroll.adapter = null
        super.onDestroyView()
    }
}
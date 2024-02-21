package com.connectcrew.presentation.screen.feature.project.projectintroduction.enrollmentmanagment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.project.enrollmentManagement.ProjectEnrollmentManagementAdapter
import com.connectcrew.presentation.databinding.FragmentProjectEnrollmentsBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.widget.GridviewItemDecoration
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectEnrollmentsFragment : BaseFragment<FragmentProjectEnrollmentsBinding>(R.layout.fragment_project_enrollments) {

    private val projectEnrollmentsViewModel: ProjectEnrollmentsViewModel by viewModels()

    private val projectEnrollmentManagementAdapter by lazy {
        ProjectEnrollmentManagementAdapter(projectEnrollmentsViewModel::navigateToEnrollmentPart)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectEnrollmentsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        with(dataBinding) {
            rvProjectEnrollMember.apply {
                adapter = projectEnrollmentManagementAdapter
                layoutManager = if (resources.getBoolean(R.bool.isTablet)) {
                    addItemDecoration(GridviewItemDecoration(6, 6, 6, 6, R.layout.item_project_enrollment_management))
                    GridLayoutManager(requireContext(), 2)
                } else {
                    addItemDecoration(RecyclerviewItemDecoration(0, 12, 0, 0, R.layout.item_project_enrollment_management))
                    LinearLayoutManager(requireContext())
                }

                setHasFixedSize(true)
            }
        }
    }

    private fun initListener() {
        dataBinding.tlProjectEnrollments.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectEnrollmentsViewModel.enrollmentMembers.collect {
                    projectEnrollmentManagementAdapter.submitList(it)
                }
            }

            launch {
                projectEnrollmentsViewModel.navigateToEnrollmentPart.collect { (projectId, projectEnrollmentMember) ->
                    findNavController().safeNavigate(ProjectEnrollmentsFragmentDirections.actionProjectEnrollmentsFragmentToProjectEnrollmentPartsFragment(projectId, projectEnrollmentMember))
                }
            }
        }
    }
}
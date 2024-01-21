package com.connectcrew.presentation.screen.feature.project.projectintroduction

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.project.introduction.ProjectIntroductionAdapter
import com.connectcrew.presentation.databinding.FragmentProjectDetailIntroductionBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.project.ProjectDetailContainerFragmentDirections
import com.connectcrew.presentation.screen.feature.project.ProjectDetailContainerViewModel
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectDetailIntroductionFragment : BaseFragment<FragmentProjectDetailIntroductionBinding>(R.layout.fragment_project_detail_introduction) {

    private val projectDetailContainerViewModel: ProjectDetailContainerViewModel by hiltNavGraphViewModels(R.id.nav_project_detail)

    private val projectDetailIntroductionViewModel: ProjectDetailIntroductionViewModel by viewModels()

    private val projectIntroductionAdapter by lazy {
        ProjectIntroductionAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectDetailIntroductionViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initObserver()
    }

    private fun initView() {
        dataBinding.rvProjectIntroduction.apply {
            adapter = projectIntroductionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectDetailContainerViewModel.projectId.filterNotNull().collect {
                    projectDetailIntroductionViewModel.setProjectId(it)
                }
            }

            launch {
                projectDetailIntroductionViewModel.isProjectLeader.filterNotNull().collect {
                    projectDetailContainerViewModel.setProjectLeader(it)
                }
            }

            launch {
                projectDetailIntroductionViewModel.projectDetail
                    .filterNotNull()
                    .onEach { projectDetailContainerViewModel.setProjectFeedDetail(it) }
                    .map { projectDetailIntroductionViewModel.createProjectDetailIntroductionUiModels(it) }
                    .collect { projectIntroductionAdapter.submitList(it) }
            }

            launch {
                projectDetailContainerViewModel.invalidateProjectDetail.collect {
                    projectDetailIntroductionViewModel.onRefresh()
                }
            }

            launch {
                projectDetailIntroductionViewModel.navigateToProjectEnrollDialog.collect {
                    findNavController().safeNavigate(ProjectDetailContainerFragmentDirections.actionProjectDetailContainerFragmentToNavProjectEnrollment(it.id, it))
                }
            }

            launch {
                projectDetailIntroductionViewModel.navigateToProjectManagementDialog.collect {
                    findNavController().safeNavigate(ProjectDetailContainerFragmentDirections.actionProjectDetailContainerFragmentToProjectManagementBottomSheetDialogFragment())
                }
            }

            launch {
                projectDetailIntroductionViewModel.loading.collect {
                    if (it) showLoadingDialog() else hideLoadingDialog()
                }
            }

            launch {
                projectDetailIntroductionViewModel.message.collect {
                    showToast(it)
                }
            }

            launch {
                projectDetailIntroductionViewModel.messageRes.collect {
                    showToast(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        dataBinding.rvProjectIntroduction.adapter = null
        super.onDestroyView()
    }

    companion object {

        fun getInstance(): ProjectDetailIntroductionFragment {
            return ProjectDetailIntroductionFragment()
        }
    }
}
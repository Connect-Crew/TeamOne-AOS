package com.connectcrew.presentation.screen.feature.project.projectintroduction

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.project.introduction.ProjectIntroductionAdapter
import com.connectcrew.presentation.databinding.FragmentProjectDetailIntroductionBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.project.ProjectDetailContainerViewModel
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
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
                projectDetailIntroductionViewModel.projectDetail
                    .filterNotNull()
                    .map { projectDetailIntroductionViewModel.createProjectDetailIntroductionUiModels(it) }
                    .collect { projectIntroductionAdapter.submitList(it) }
            }

            launch {
                projectDetailIntroductionViewModel.navigateToProjectEnrollDialog.collect {
                    // TODO:: 지원하기 바텀시트 호출
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
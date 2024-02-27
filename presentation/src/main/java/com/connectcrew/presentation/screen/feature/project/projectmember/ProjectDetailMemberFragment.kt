package com.connectcrew.presentation.screen.feature.project.projectmember

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.project.member.ProjectMemberAdapter
import com.connectcrew.presentation.databinding.FragmentProjectDetailMemberBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.project.ProjectDetailContainerViewModel
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectDetailMemberFragment : BaseFragment<FragmentProjectDetailMemberBinding>(R.layout.fragment_project_detail_member) {

    private val projectDetailContainerViewModel: ProjectDetailContainerViewModel by hiltNavGraphViewModels(R.id.nav_project_detail)

    private val projectDetailMemberViewModel: ProjectDetailMemberViewModel by hiltNavGraphViewModels(R.id.nav_project_detail)

    private val projectMemberAdapter: ProjectMemberAdapter by lazy {
        ProjectMemberAdapter(
            onClickKickMember = projectDetailMemberViewModel::navigateToMemberKickDialog,
            onClickMemberProfile = projectDetailMemberViewModel::navigateToMemberProfile,
            onClickRepresentProject = projectDetailMemberViewModel::navigateToMemberRepresentProject
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(dataBinding) {
            viewModel = projectDetailMemberViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initObserver()
    }

    private fun initView() {
        with(dataBinding.rvMembers) {
            adapter = projectMemberAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(RecyclerviewItemDecoration(0, 12, 0, 0, R.layout.item_project_member))
            setHasFixedSize(true)
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectDetailContainerViewModel.projectId.filterNotNull().collect {
                    projectDetailMemberViewModel.setProjectId(it)
                }
            }
            launch {
                projectDetailContainerViewModel.isProjectLeader.filterNotNull().collect {
                    projectDetailMemberViewModel.setProjectLeader(it)
                }
            }
            launch {
                projectDetailMemberViewModel.projectMembers.collect {
                    projectMemberAdapter.submitList(it)
                }
            }
        }
    }

    companion object {

        fun getInstance(): ProjectDetailMemberFragment {
            return ProjectDetailMemberFragment()
        }
    }
}
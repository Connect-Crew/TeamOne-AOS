package com.connectcrew.presentation.screen.feature.project.projectmember

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.project.member.ProjectMemberAdapter
import com.connectcrew.presentation.databinding.FragmentProjectDetailMemberBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.project.ProjectDetailContainerFragmentDirections
import com.connectcrew.presentation.screen.feature.project.ProjectDetailContainerViewModel
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.view.createAlert
import com.connectcrew.presentation.util.view.dialogViewBuilder
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectDetailMemberFragment : BaseFragment<FragmentProjectDetailMemberBinding>(R.layout.fragment_project_detail_member) {

    private val projectDetailContainerViewModel: ProjectDetailContainerViewModel by hiltNavGraphViewModels(R.id.nav_project_detail)

    private val projectDetailMemberViewModel: ProjectDetailMemberViewModel by viewModels()

    private val projectMemberAdapter: ProjectMemberAdapter by lazy {
        ProjectMemberAdapter(
            onClickKickMember = projectDetailMemberViewModel::navigateToMemberKickDialog,
            onClickMemberProfile = projectDetailMemberViewModel::navigateToMemberProfile
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
                projectDetailContainerViewModel.invalidateProjectDetail.collect {
                    projectDetailMemberViewModel.onRefresh()
                }
            }

            launch {
                projectDetailMemberViewModel.projectMembers.collect {
                    projectMemberAdapter.submitList(it)
                }
            }

            launch {
                projectDetailMemberViewModel.navigateToMemberKickDialog.collect { member ->
                    createAlert(requireContext())
                        .dialogViewBuilder(
                            titleRes = R.string.project_detail_member_kick_title,
                            titleResArg = member.profile.nickname,
                            descriptionRes = R.string.project_detail_member_kick_description,
                            positiveButtonTextRes = R.string.project_detail_member_kick,
                            iconTint = R.color.color_d62246,
                            iconDrawableRes = R.drawable.ic_warning,
                            onClickPositiveButton = { projectDetailMemberViewModel.navigateToMemberKickReasonDialog(member) }
                        ).show()
                }
            }

            launch {
                projectDetailMemberViewModel.navigateToMemberKickReasonDialog.collect { (projectId, memberId) ->
                    findNavController().safeNavigate(ProjectDetailContainerFragmentDirections.actionProjectDetailContainerFragmentToProjectMemberKickReasonDialog(projectId, memberId))
                }
            }

            launch {
                projectDetailMemberViewModel.navigateToMemberProfile.collect { memberId ->
                    //::TODO 상대방 프로필 화면으로 이동
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
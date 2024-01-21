package com.connectcrew.presentation.screen.feature.project

import android.os.Bundle
import android.view.Gravity.END
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.menuspinner.MenuSpinnerAdapter
import com.connectcrew.presentation.databinding.FragmentProjectDetailContainerBinding
import com.connectcrew.presentation.model.project.ProjectFeedDetailCategory
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.project.projectchat.ProjectDetailChatFragment
import com.connectcrew.presentation.screen.feature.project.projectintroduction.ProjectDetailIntroductionFragment
import com.connectcrew.presentation.screen.feature.project.projectmember.ProjectDetailMemberFragment
import com.connectcrew.presentation.util.Const.KEY_IS_PROJECT_UPDATE
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.toPx
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectDetailContainerFragment : BaseFragment<FragmentProjectDetailContainerBinding>(R.layout.fragment_project_detail_container) {

    private val projectDetailContainerViewModel: ProjectDetailContainerViewModel by hiltNavGraphViewModels(R.id.nav_project_detail)

    private val menuSpinnerAdapter by lazy {
        MenuSpinnerAdapter(requireContext(), resources.getStringArray(R.array.project_detail_spinner).toList()) {
            projectDetailContainerViewModel.navigateToProjectReportReasonDialog()
        }
    }

    private val pagedChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            projectDetailContainerViewModel.setSelectedProjectDetailCategory(
                when (position) {
                    0 -> ProjectFeedDetailCategory.INTRODUCTION
                    1 -> ProjectFeedDetailCategory.MEMBER
                    2 -> ProjectFeedDetailCategory.CHAT
                    else -> return
                }
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initListener()
        initObserver()
        initNavBackStackObserve()
    }

    private fun initView() {
        with(dataBinding) {
            (tlProjectContainer.menu.findItem(R.id.menu_more) as MenuItem).let {
                it.isVisible = false
                (it.actionView as Spinner).apply {
                    setPadding(0)
                    dropDownVerticalOffset = 46.toPx(requireContext())
                    background = null
                    gravity = END
                    adapter = menuSpinnerAdapter
                }
            }

            vpProjectContainer.apply {
                adapter = ProjectDetailContainerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
                setPageTransformer(MarginPageTransformer(8.toPx(requireContext())))
            }

            TabLayoutMediator(tabProjectContainer, vpProjectContainer) { tab, position ->
                tab.text = resources.getString(PROJECT_FEED_DETAIL_TITLES[position])
            }.attach()
        }
    }

    private fun initListener() {
        with(dataBinding) {
            tlProjectContainer.setNavigationOnClickListener {
                if (!findNavController().navigateUp()) {
                    requireActivity().finish()
                }
            }

            vpProjectContainer.registerOnPageChangeCallback(pagedChangeCallback)
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectDetailContainerViewModel.projectId.collect()
            }

            launch {
                projectDetailContainerViewModel.isProjectLeader.filterNotNull().collect {
                    dataBinding.tlProjectContainer.menu.findItem(R.id.menu_more).isVisible = !it
                }
            }

            launch {
                projectDetailContainerViewModel.navigateToProjectReportReasonDialog.collect {
                    findNavController().safeNavigate(ProjectDetailContainerFragmentDirections.actionProjectDetailContainerFragmentToProjectReportReasonAlertDialog())
                }
            }

            launch {
                projectDetailContainerViewModel.navigateToProjectRecruit.collect {
                    if (findNavController().currentDestination?.id == R.id.projectManagementBottomSheetDialogFragment) findNavController().navigateUp()
                    // TODO:: 지원자 현황으로 이동
                }
            }

            launch {
                projectDetailContainerViewModel.navigateToProjectUpdate.collect {
                    if (findNavController().currentDestination?.id == R.id.projectManagementBottomSheetDialogFragment) findNavController().navigateUp()
                    findNavController().safeNavigate(ProjectDetailContainerFragmentDirections.actionProjectDetailContainerFragmentToProjectWriteContainerFragment(it))
                }
            }

            launch {
                projectDetailContainerViewModel.navigateToProjectRemovePopup.collect {
                    // TODO:: 프로젝트 삭제 확인 팝업으로 이동
                }
            }

            launch {
                projectDetailContainerViewModel.navigateToProjectCompletedPopup.collect {
                    // TODO:: 프로젝트 완료 확인 팝업으로 이동
                }
            }
        }
    }

    private fun initNavBackStackObserve() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.projectDetailContainerFragment)

        val resultObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!navBackStackEntry.savedStateHandle.contains(KEY_IS_PROJECT_UPDATE)) return@LifecycleEventObserver
                val isUpdate: Boolean = navBackStackEntry.savedStateHandle[KEY_IS_PROJECT_UPDATE] ?: false
                if (isUpdate) projectDetailContainerViewModel.invalidateProjectDetail()
                navBackStackEntry.savedStateHandle.remove<Boolean>(KEY_IS_PROJECT_UPDATE)
            }
        }

        navBackStackEntry.lifecycle.addObserver(resultObserver)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(resultObserver)
            }
        })
    }

    override fun onDestroyView() {
        with(dataBinding) {
            vpProjectContainer.unregisterOnPageChangeCallback(pagedChangeCallback)
            (tlProjectContainer.menu.findItem(R.id.menu_more) as MenuItem).let {
                (it.actionView as Spinner).adapter = null
            }
        }
        super.onDestroyView()
    }

    inner class ProjectDetailContainerAdapter(fm: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fm, lifecycle) {

        override fun createFragment(position: Int): Fragment = PROJECT_FEED_DETAIL_PAGES[position]()

        override fun getItemCount() = PROJECT_FEED_DETAIL_PAGES.size
    }


    companion object {
        private val PROJECT_FEED_DETAIL_TITLES = arrayOf(
            R.string.project_detail_introduction,
            R.string.project_detail_member,
            R.string.project_detail_chat,
        )

        private val PROJECT_FEED_DETAIL_PAGES = arrayOf(
            { ProjectDetailIntroductionFragment.getInstance() },
            { ProjectDetailMemberFragment.getInstance() },
            { ProjectDetailChatFragment.getInstance() }
        )
    }
}
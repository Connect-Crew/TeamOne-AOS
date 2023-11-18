package com.connectcrew.presentation.screen.feature.project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentProjectDetailContainerBinding
import com.connectcrew.presentation.model.project.ProjectFeedDetailCategory
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.project.projectchat.ProjectDetailChatFragment
import com.connectcrew.presentation.screen.feature.project.projectintroduction.ProjectDetailIntroductionFragment
import com.connectcrew.presentation.screen.feature.project.projectmember.ProjectDetailMemberFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnMenuItemSingleClickListener
import com.connectcrew.presentation.util.toPx
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ProjectDetailContainerFragment : BaseFragment<FragmentProjectDetailContainerBinding>(R.layout.fragment_project_detail_container) {

    private val projectDetailContainerViewModel: ProjectDetailContainerViewModel by hiltNavGraphViewModels(R.id.nav_project_detail)

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
    }

    private fun initView() {
        with(dataBinding) {
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
            tlProjectContainer.setOnMenuItemSingleClickListener {
                when (it.itemId) {
                    R.id.menu_more -> {
                        // TODO :: 신고하기 로직 추가
                        true
                    }

                    else -> false
                }
            }


            vpProjectContainer.registerOnPageChangeCallback(pagedChangeCallback)
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            projectDetailContainerViewModel.projectId.collect()
        }
    }

    override fun onDestroyView() {
        dataBinding.vpProjectContainer.unregisterOnPageChangeCallback(pagedChangeCallback)
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
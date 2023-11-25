package com.connectcrew.presentation.screen.feature.main.home

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ContextMenu
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.connectcrew.domain.util.UnAuthorizedException
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.common.TeamOneLoadAdapter
import com.connectcrew.presentation.adapter.home.category.HomeCategoryAdapter
import com.connectcrew.presentation.adapter.home.project.HomeProjectFeedAdapter
import com.connectcrew.presentation.databinding.FragmentHomeBinding
import com.connectcrew.presentation.model.project.ProjectCategoryType
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.listener.setOnMenuItemSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    private val homeCategoryAdapter by lazy {
        HomeCategoryAdapter {
            homeViewModel.setSelectedCategory(it.category)
        }
    }
    private val homeProjectFeedAdapter by lazy {
        HomeProjectFeedAdapter(
            onClickFavoriteProjectFeed = { homeViewModel.setProjectFeedLike(it) },
            onClickProjectFeed = { homeViewModel.navigateToProjectFeedDetail(it) }
        )
    }

    private val projectCategoryScrollListener: RecyclerView.SmoothScroller by lazy {
        object : LinearSmoothScroller(requireContext()) {
            override fun calculateDtToFit(viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int): Int {
                return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
                return (KEY_CATEGORY_SCROLL_DURATION / (displayMetrics?.densityDpi ?: 0))
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        with(dataBinding) {
            rvCategory.apply {
                adapter = homeCategoryAdapter
                itemAnimator = null
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(RecyclerviewItemDecoration(0, 0, 0, 12, R.layout.item_project_category))
            }

            rvProjectFeed.apply {
                setHasFixedSize(true)
                adapter = homeProjectFeedAdapter.withLoadStateFooter(TeamOneLoadAdapter { homeProjectFeedAdapter.retry() })
                layoutManager = if (resources.getBoolean(R.bool.isTablet)) {
                    GridLayoutManager(requireContext(), 2).apply {
                        isMeasurementCacheEnabled = false
                        this.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return 1
                            }
                        }
                    }
                } else {
                    LinearLayoutManager(requireContext())
                }
            }
        }
    }

    private fun initListener() {
        with(dataBinding) {
            tlHome.apply {
                setOnMenuItemSingleClickListener {
                    when (it.itemId) {
                        R.id.menu_notification -> {
                            //TODO:: 알림 화면으로 이동
                            true
                        }

                        R.id.menu_search -> {
                            findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToSearchFragment())
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                homeViewModel.projectCategoryItems.collect {
                    homeCategoryAdapter.submitList(it)
                }
            }

            launch {
                homeViewModel.projectFeeds.collectLatest {
                    homeProjectFeedAdapter.submitData(it)
                }
            }

            launch {
                homeProjectFeedAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .collect { loadState ->
                        val state = loadState.source.refresh

                        with(dataBinding) {
                            viewLoading.root.isVisible = state is LoadState.Loading
                            rvProjectFeed.isVisible = state !is LoadState.Error
                            llProjectFeedEmpty.isVisible = state is LoadState.NotLoading && homeProjectFeedAdapter.itemCount == 0

                            if (state is LoadState.Error && state.error is UnAuthorizedException) {
                                homeViewModel.refreshUserToken()
                            } else {
                                viewNetworkError.root.isVisible = state is LoadState.Error
                            }
                        }
                    }
            }

            launch {
                homeViewModel.scrollToCenterForProjectCategory.collect {
                    with(dataBinding.rvCategory) {
                        (layoutManager as LinearLayoutManager).startSmoothScroll(projectCategoryScrollListener.apply {
                            targetPosition = ProjectCategoryType.values().toList().indexOf(it)
                        })
                    }
                }
            }

            launch {
                homeViewModel.navigateToProjectFeedDetail.collect {
                    findNavController().safeNavigate(HomeFragmentDirections.actionHomeFragmentToNavProjectDetail(it))
                }
            }
        }
    }

    override fun onDestroyView() {
        dataBinding.apply {
            rvCategory.adapter = null
            rvProjectFeed.adapter = null
        }
        super.onDestroyView()
    }

    companion object {
        private const val KEY_CATEGORY_SCROLL_DURATION = 80f
    }
}
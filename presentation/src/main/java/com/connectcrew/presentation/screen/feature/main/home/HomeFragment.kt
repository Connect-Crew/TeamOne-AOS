package com.connectcrew.presentation.screen.feature.main.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.domain.util.UnAuthorizedException
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.common.TeamOneLoadAdapter
import com.connectcrew.presentation.adapter.home.category.HomeCategoryAdapter
import com.connectcrew.presentation.adapter.home.project.HomeProjectFeedAdapter
import com.connectcrew.presentation.databinding.FragmentHomeBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModels()

    private val homeCategoryAdapter by lazy { HomeCategoryAdapter() }
    private val homeProjectFeedAdapter by lazy { HomeProjectFeedAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = homeViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initObserver()
    }

    private fun initView() {
        with(dataBinding) {
            rvCategory.apply {
                adapter = homeCategoryAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(RecyclerviewItemDecoration(0, 0, 0, 12, R.layout.item_project_category))
            }

            rvProjectFeed.apply {
                setHasFixedSize(true)
                adapter = homeProjectFeedAdapter.withLoadStateFooter(
                    TeamOneLoadAdapter { homeProjectFeedAdapter.retry() }
                )
                layoutManager = if (resources.equals(R.bool.isTablet)) {
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

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                homeViewModel.projectCategoryTypes.collect {
                    homeCategoryAdapter.submitList(it)
                }
            }

            launch {
                homeViewModel.projectFeeds.collectLatest {
                    homeProjectFeedAdapter.submitData(it)
                }
            }

            launch {
                homeProjectFeedAdapter.loadStateFlow.collectLatest { loadState ->
                    val state = loadState.source.refresh

                    with(dataBinding) {
                        pbLoading.isVisible = state is LoadState.Loading
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
        }
    }

    override fun onDestroyView() {
        dataBinding.apply {
            rvCategory.adapter = null
            rvProjectFeed.adapter = null
        }
        super.onDestroyView()
    }
}
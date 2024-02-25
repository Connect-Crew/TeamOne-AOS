package com.connectcrew.presentation.screen.feature.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.search.SearchHistoryAdapter
import com.connectcrew.presentation.databinding.FragmentSearchBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.hideKeyboard
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    private val searchViewModel: SearchViewModel by viewModels()

    private val searchHistoryAdapter: SearchHistoryAdapter by lazy {
        SearchHistoryAdapter(
            onClickDeleteHistory = searchViewModel::deleteSearchHistory,
            onClickSearchHistory = searchViewModel::searchProjectFeed
        )
    }

    private val userSearchKeywordChangeListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = searchViewModel.viewModelScope,
            onDebounceEditTextChange = searchViewModel::setSearchingKeyword
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = searchViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        dataBinding.rvSearchHistory.apply {
            adapter = searchHistoryAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private fun initListener() {
        with(dataBinding) {
            ivNavigate.setOnSingleClickListener {
                if (!findNavController().navigateUp()) {
                    requireActivity().finish()
                }
            }

            ivSearch.setOnSingleClickListener {
                searchViewModel.searchProjectFeed(tietSearchKeyword.text.toString())
                tietSearchKeyword.hideKeyboard(true)
            }

            tietSearchKeyword.addTextChangedListener(userSearchKeywordChangeListener)
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                searchViewModel.searchHistories.collectLatest {
                    searchHistoryAdapter.submitData(it)
                }
            }

            launch {
                searchHistoryAdapter.loadStateFlow
                    .distinctUntilChangedBy { it.refresh }
                    .collect { loadState ->
                        val state = loadState.source.refresh

                        dataBinding.llSearchHistoryEmpty.isVisible = state is LoadState.NotLoading && searchHistoryAdapter.itemCount == 0
                    }
            }

            launch {
                searchViewModel.message.collect {
                    showToast(it)
                }
            }
        }
    }
}
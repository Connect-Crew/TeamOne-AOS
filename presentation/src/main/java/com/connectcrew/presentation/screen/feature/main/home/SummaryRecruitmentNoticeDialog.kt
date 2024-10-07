package com.connectcrew.presentation.screen.feature.main.home

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.home.summarymember.SummaryMemberAdapter
import com.connectcrew.presentation.databinding.DialogSummaryRecruitmentNoticeBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class SummaryRecruitmentNoticeDialog : BaseAlertDialogFragment<DialogSummaryRecruitmentNoticeBinding>() {

    override val layoutResId: Int = R.layout.dialog_summary_recruitment_notice

    private val homeViewModel: HomeViewModel by hiltNavGraphViewModels(R.id.nav_home)

    private val summaryMemberAdapter by lazy { SummaryMemberAdapter() }

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
        dataBinding.rvMember.apply {
            adapter = summaryMemberAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initListener() {
        with(dataBinding) {
            btnCancel.setOnSingleClickListener {
                findNavController().navigateUp()
            }

            btnDetail.setOnSingleClickListener {
                homeViewModel.navigateToProjectFeedDetail(homeViewModel.selectedProjectFeed.value!!)
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            homeViewModel.selectedProjectFeed.filterNotNull().collect {
                summaryMemberAdapter.submitList(it.recruitStatus)
            }
        }
    }

    override fun onDestroyView() {
        dataBinding.rvMember.adapter = null
        super.onDestroyView()
    }
}
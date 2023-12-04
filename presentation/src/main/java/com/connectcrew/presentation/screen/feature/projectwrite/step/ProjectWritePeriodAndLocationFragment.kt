package com.connectcrew.presentation.screen.feature.projectwrite.step

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.projectwrite.ProjectLocationAdapter
import com.connectcrew.presentation.databinding.FragmentProjectWritePeriodAndLocationBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.projectwrite.ProjectWriteContainerViewModel
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull

@AndroidEntryPoint
class ProjectWritePeriodAndLocationFragment : BaseFragment<FragmentProjectWritePeriodAndLocationBinding>(R.layout.fragment_project_write_period_and_location) {

    private val projectWriteContainerViewModel: ProjectWriteContainerViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    private val projectWriteLocationAdapter by lazy {
        ProjectLocationAdapter(projectWriteContainerViewModel::setProjectLocation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectWriteContainerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initObserver()
        initListener()
    }

    private fun initView() {
        dataBinding.rvLocation.apply {
            adapter = projectWriteLocationAdapter
            layoutManager = GridLayoutManager(requireContext(), if (resources.getBoolean(R.bool.isTablet)) 5 else 3)
            itemAnimator = null
            setHasFixedSize(true)
        }
    }

    private fun initListener() {
        with(dataBinding) {
            btnPrevious.setOnSingleClickListener {
                findNavController().navigateUp()
            }

            btnNext.setOnSingleClickListener {
                findNavController().safeNavigate(ProjectWritePeriodAndLocationFragmentDirections.actionProjectWritePeriodAndLocationFragmentToProjectWritePurposeAndCareerFragment())
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            combine(
                projectWriteContainerViewModel.projectContainerInfo.filterNotNull(),
                projectWriteContainerViewModel.projectLocation,
                ::Pair
            ).collect { (projectInfoList, selectInfo) ->
                projectWriteLocationAdapter.submitList(projectInfoList.regions.map { it.copy(isSelected = it == selectInfo) })
            }
        }
    }
}
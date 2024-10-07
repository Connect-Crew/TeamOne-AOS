package com.connectcrew.presentation.screen.feature.projectwrite.step

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.projectwrite.ProjectFieldAdapter
import com.connectcrew.presentation.databinding.FragmentProjectWriteFieldBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.projectwrite.ProjectWriteContainerViewModel
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectWriteFieldFragment : BaseFragment<FragmentProjectWriteFieldBinding>(R.layout.fragment_project_write_field) {

    private val projectWriteContainerViewModel: ProjectWriteContainerViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    private val projectWriteFieldAdapter by lazy {
        ProjectFieldAdapter(projectWriteContainerViewModel::setProjectField)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectWriteContainerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        dataBinding.rvProjectField.apply {
            addItemDecoration(RecyclerviewItemDecoration(0, 20, 0, 20, R.layout.item_project_write_field))
            adapter = projectWriteFieldAdapter
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
                findNavController().safeNavigate(ProjectWriteFieldFragmentDirections.actionProjectWriteFieldFragmentToProjectWritePostFragment())
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                combine(
                    projectWriteContainerViewModel.projectContainerInfo.filterNotNull(),
                    projectWriteContainerViewModel.projectSelectedFiled,
                    ::Pair
                ).collect { (projectInfoList, selectInfo) ->
                    projectWriteFieldAdapter.submitList(projectInfoList.category.map { filed -> filed.copy(isSelected = selectInfo.any { it.category == filed.category }) })
                }
            }

            launch {
                projectWriteContainerViewModel.message.collect {
                    showToast(it)
                }
            }
        }
    }
}
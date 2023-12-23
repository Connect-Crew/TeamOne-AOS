package com.connectcrew.presentation.screen.feature.projectwrite.step

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentProjectWriteTitleBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.projectwrite.ProjectWriteContainerViewModel
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.setTextInputError
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectWriteTitleFragment : BaseFragment<FragmentProjectWriteTitleBinding>(R.layout.fragment_project_write_title) {

    private val projectWriteContainerViewModel: ProjectWriteContainerViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    private val projectTitleTextChangeListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = projectWriteContainerViewModel.viewModelScope,
            onDebounceEditTextChange = projectWriteContainerViewModel::setProjectTitle
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectWriteContainerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
        with(dataBinding) {
            tietProjectName.addTextChangedListener(projectTitleTextChangeListener)

            btnNext.setOnSingleClickListener {
                findNavController().safeNavigate(ProjectWriteTitleFragmentDirections.actionProjectWriteTitleFragmentToProjectWritePeriodAndLocationFragment())
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            projectWriteContainerViewModel.projectTitleEditTextState.collect {
                dataBinding.tilProjectName.setTextInputError(it)
            }
        }
    }
}
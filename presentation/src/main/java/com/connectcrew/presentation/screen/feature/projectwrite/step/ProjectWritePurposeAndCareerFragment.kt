package com.connectcrew.presentation.screen.feature.projectwrite.step

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentProjectWritePurposeAndCareerBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.projectwrite.ProjectWriteContainerViewModel
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectWritePurposeAndCareerFragment : BaseFragment<FragmentProjectWritePurposeAndCareerBinding>(R.layout.fragment_project_write_purpose_and_career) {

    private val projectWriteContainerViewModel: ProjectWriteContainerViewModel by viewModels({ requireParentFragment().requireParentFragment() })

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
            spCareerMin.apply {
                spinnerOutsideTouchListener = OnSpinnerOutsideTouchListener { _, _ -> dismiss() }
                setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                    projectWriteContainerViewModel.setProjectCareer(true, text)
                }
            }

            spCareerMax.apply {
                spinnerOutsideTouchListener = OnSpinnerOutsideTouchListener { _, _ -> dismiss() }
                setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                    projectWriteContainerViewModel.setProjectCareer(false, text)
                }
            }

            btnPrevious.setOnSingleClickListener {
                findNavController().navigateUp()
            }

            btnNext.setOnSingleClickListener {
                findNavController().safeNavigate(ProjectWritePurposeAndCareerFragmentDirections.actionProjectWritePurposeAndCareerFragmentToProjectWriteFieldFragment())
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectWriteContainerViewModel.isNoLimitCheck.collect {
                    with(dataBinding) {
                        spCareerMin.isEnabled = !it
                        spCareerMax.isEnabled = !it
                    }
                }
            }

            launch {
                projectWriteContainerViewModel.projectMinCareer.collect {
                    dataBinding.spCareerMin.selectItemByIndex(resources.getStringArray(R.array.project_write_career_spinner).toList().indexOf(it.value))
                }
            }

            launch {
                projectWriteContainerViewModel.projectMaxCareer.collect {
                    dataBinding.spCareerMax.selectItemByIndex(resources.getStringArray(R.array.project_write_career_spinner).toList().indexOf(it.value))
                }
            }

            launch {
                projectWriteContainerViewModel.messageRes.collect {
                    showToast(it)
                }
            }
        }
    }
}
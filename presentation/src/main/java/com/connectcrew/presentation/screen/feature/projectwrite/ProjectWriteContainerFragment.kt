package com.connectcrew.presentation.screen.feature.projectwrite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentProjectWriteContainerBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.base.BaseViewModel
import com.connectcrew.presentation.util.Const
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnMenuItemSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.view.createAlert
import com.connectcrew.presentation.util.view.dialogViewBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectWriteContainerFragment : BaseFragment<FragmentProjectWriteContainerBinding>(R.layout.fragment_project_write_container) {

    private val projectWriteContainerViewModel: ProjectWriteContainerViewModel by viewModels()

    private val childNavController: NavController?
        get() = childFragmentManager.findFragmentById(R.id.nav_host_child_fragment_container)?.findNavController()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childNavController?.setGraph(R.navigation.nav_project_write)

        dataBinding.apply {
            viewModel = projectWriteContainerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
        with(dataBinding) {
            tlProjectWriteContainer.setOnMenuItemSingleClickListener {
                when (it.itemId) {
                    R.id.menu_close -> {
                        createAlert(requireContext())
                            .dialogViewBuilder(
                                titleRes = if (projectWriteContainerViewModel.isUpdateProject) R.string.project_write_update_exit_title else R.string.project_write_exit_title,
                                descriptionRes = R.string.project_write_exit_description,
                                strokeColor = R.color.color_d62246,
                                iconDrawableRes = R.drawable.ic_warning,
                                iconTint = R.color.color_d62246,
                                onClickPositiveButton = { projectWriteContainerViewModel.navigateToExit() }
                            )
                            .show()
                        true
                    }

                    else -> false
                }
            }

            childNavController?.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.projectWriteTitleFragment -> 1
                    R.id.projectWritePeriodAndLocationFragment -> 2
                    R.id.projectWritePurposeAndCareerFragment -> 3
                    R.id.projectWriteFieldFragment -> 4
                    R.id.projectWritePostFragment -> 5
                    else -> -1
                }.let {
                    projectWriteContainerViewModel.setWriteProgress(it)
                }
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                combine(
                    flowOf(projectWriteContainerViewModel.projectFeedDetail),
                    projectWriteContainerViewModel.projectWriteInitializerUiState,
                    ::Pair
                ).filter { (projectFeed, uiState) ->
                    projectFeed != null && uiState == BaseViewModel.InitializerUiState.Success
                }.collect { (projectFeed, _) ->
                    if (projectFeed != null) {
                        projectWriteContainerViewModel.updateProject(projectFeed)
                    }
                }
            }

            launch {
                projectWriteContainerViewModel.navigateToExit.collect {
                    findNavController().navigateUp()
                }
            }

            launch {
                projectWriteContainerViewModel.navigateToProjectDetail.collect {
                    findNavController().run {
                        if (previousBackStackEntry?.destination?.id == R.id.projectDetailContainerFragment) {
                            findNavController().run {
                                getBackStackEntry(R.id.projectDetailContainerFragment).savedStateHandle.apply { set(Const.KEY_IS_PROJECT_UPDATE, true) }
                                navigateUp()
                            }
                        } else {
                            safeNavigate(ProjectWriteContainerFragmentDirections.actionProjectWriteContainerFragmentToNavProjectDetail(it))
                        }
                    }
                }
            }

            launch {
                projectWriteContainerViewModel.loading.collect {
                    if (it) showLoadingDialog() else hideLoadingDialog()
                }
            }

            launch {
                projectWriteContainerViewModel.messageRes.collect {
                    showToast(it)
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
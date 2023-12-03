package com.connectcrew.presentation.screen.feature.projectwrite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentProjectWriteContainerBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.listener.setOnMenuItemSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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
    }

    private fun initListener() {
        with(dataBinding) {
            tlProjectWriteContainer.setOnMenuItemSingleClickListener {
                when (it.itemId) {
                    R.id.menu_close -> {
                        findNavController().navigateUp()
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
                    else -> return@addOnDestinationChangedListener
                }.let {
                    projectWriteContainerViewModel.setWriteProgress(it)
                }
            }
        }
    }
}
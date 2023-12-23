package com.connectcrew.presentation.screen.feature.projectwrite.step

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.projectwrite.ProjectBannerAdapter
import com.connectcrew.presentation.adapter.projectwrite.ProjectMemberPartRecruitmentAdapter
import com.connectcrew.presentation.adapter.projectwrite.ProjectTechStackAdapter
import com.connectcrew.presentation.databinding.FragmentProjectWritePostBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.screen.feature.projectwrite.ProjectWriteContainerViewModel
import com.connectcrew.presentation.util.Const.KEY_SELECTED_MEDIA_PATHS
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import com.skydoves.powerspinner.OnSpinnerOutsideTouchListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectWritePostFragment : BaseFragment<FragmentProjectWritePostBinding>(R.layout.fragment_project_write_post) {

    private val projectWriteContainerViewModel: ProjectWriteContainerViewModel by viewModels({ requireParentFragment().requireParentFragment() })

    private val projectWriteIntroductionTextChangListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = projectWriteContainerViewModel.viewModelScope,
            onDebounceEditTextChange = projectWriteContainerViewModel::setProjectIntroduction
        )
    }

    private val projectWriteTechStackEditTextActionListener by lazy {
        TextView.OnEditorActionListener { textView, actionId, _ ->
            return@OnEditorActionListener if (actionId == EditorInfo.IME_ACTION_DONE && textView.text.trim().isNotEmpty()) {
                projectWriteContainerViewModel.setProjectTechStacks(textView.text.toString())
                dataBinding.tietTechStack.text?.clear()
                true
            } else {
                false
            }
        }
    }

    private val projectBannerAdapter by lazy {
        ProjectBannerAdapter { projectWriteContainerViewModel.setProjectBanner(listOf(it), true) }
    }

    private val projectProjectMemberPartRecruitmentAdapter by lazy {
        ProjectMemberPartRecruitmentAdapter(
            coroutineScope = lifecycleScope,
            onRemoveProjectMemberPartRecruitment = projectWriteContainerViewModel::removeRecruitmentMembers,
            onUpdateProjectMemberPartRecruitmentCount = projectWriteContainerViewModel::updateRecruitmentMembersCount,
            onUpdateProjectMemberPartRecruitmentComment = projectWriteContainerViewModel::updateRecruitmentMembersComment
        )
    }

    private val projectTechStackAdapter by lazy {
        ProjectTechStackAdapter(projectWriteContainerViewModel::removeProjectTechStacks)
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
        initNavBackStackObserve()
    }

    private fun initView() {
        with(dataBinding) {
            rvBanner.apply {
                adapter = projectBannerAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(RecyclerviewItemDecoration(0, 0, 0, 6, R.layout.item_project_write_banner))
            }

            rvMemberRecruitment.apply {
                adapter = projectProjectMemberPartRecruitmentAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(RecyclerviewItemDecoration(12, 0, 0, 0, R.layout.item_project_write_member_recruitment))
            }

            rvTechStack.apply {
                adapter = projectTechStackAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(RecyclerviewItemDecoration(0, 0, 0, 8, R.layout.item_project_write_tech_stack))
            }
        }
    }

    private fun initListener() {
        with(dataBinding) {
            spLeaderPartMainCategory.apply {
                spinnerOutsideTouchListener = OnSpinnerOutsideTouchListener { _, _ -> dismiss() }
                setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                    projectWriteContainerViewModel.setProjectLeaderMainJob(text)
                }
            }

            spLeaderPartSubCategory.apply {
                spinnerOutsideTouchListener = OnSpinnerOutsideTouchListener { _, _ -> dismiss() }
                setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                    projectWriteContainerViewModel.setProjectLeaderSubJob(text)
                }
            }

            spMemeberPartMainCategory.apply {
                spinnerOutsideTouchListener = OnSpinnerOutsideTouchListener { _, _ -> dismiss() }
                setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                    projectWriteContainerViewModel.setProjectMemberMainJob(text)
                }
            }

            spMemberPartSubCategory.apply {
                spinnerOutsideTouchListener = OnSpinnerOutsideTouchListener { _, _ -> dismiss() }
                setOnSpinnerItemSelectedListener<String> { _, _, _, text ->
                    projectWriteContainerViewModel.setProjectMemberSubJob(text)
                }
            }

            tietTechStack.setOnEditorActionListener(projectWriteTechStackEditTextActionListener)

            btnPrevious.setOnSingleClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectWriteContainerViewModel.projectBannerUrls.collect {
                    projectBannerAdapter.submitList(it)
                }
            }

            launch {
                projectWriteContainerViewModel.projectContainerInfo.filterNotNull().collect {
                    val jobMainInfo = it.jobs.map { it.name }

                    with(dataBinding) {
                        spLeaderPartMainCategory.setItems(jobMainInfo)
                        spMemeberPartMainCategory.setItems(jobMainInfo)
                    }
                }
            }

            launch {
                combine(
                    projectWriteContainerViewModel.selectedLeaderMainJobCategory.filterNotNull(),
                    projectWriteContainerViewModel.selectedLeaderSubJobCategory,
                    projectWriteContainerViewModel.projectContainerInfo.filterNotNull(),
                    ::Triple
                ).collect { (jobMainInfo, jobSubInfo, projectContainerInfo) ->
                    val selectedMainItemIndex = projectContainerInfo.jobs.indexOf(jobMainInfo)
                    val selectedSubItemIndex = jobMainInfo.value.indexOf(jobSubInfo)

                    with(dataBinding) {
                        spLeaderPartMainCategory.selectItemByIndex(selectedMainItemIndex)
                        spLeaderPartSubCategory.setItems(jobMainInfo.value.map { it.name })

                        if (selectedSubItemIndex != -1) {
                            spLeaderPartSubCategory.selectItemByIndex(selectedSubItemIndex)
                        } else {
                            spLeaderPartSubCategory.clearSelectedItem()
                            projectWriteContainerViewModel.setProjectLeaderSubJob(null)
                        }
                    }
                }
            }

            launch {
                combine(
                    projectWriteContainerViewModel.selectedMemberMainJobCategory.filterNotNull(),
                    projectWriteContainerViewModel.selectedMemberSubJobCategory,
                    projectWriteContainerViewModel.projectContainerInfo.filterNotNull(),
                    ::Triple
                ).collect { (jobMainInfo, jobSubInfo, projectContainerInfo) ->
                    val selectedMainItemIndex = projectContainerInfo.jobs.indexOf(jobMainInfo)
                    val selectedSubItemIndex = jobMainInfo.value.indexOf(jobSubInfo)

                    with(dataBinding) {
                        spMemeberPartMainCategory.selectItemByIndex(selectedMainItemIndex)
                        spMemberPartSubCategory.setItems(jobMainInfo.value.map { it.name })

                        if (selectedSubItemIndex != -1) {
                            spMemberPartSubCategory.selectItemByIndex(selectedSubItemIndex)
                        } else {
                            spMemberPartSubCategory.clearSelectedItem()
                            projectWriteContainerViewModel.setProjectMemberSubJob(null)
                        }
                    }
                }
            }

            launch {
                projectWriteContainerViewModel.recruitmentMembers.collect {
                    projectProjectMemberPartRecruitmentAdapter.submitList(it)
                }
            }

            launch {
                projectWriteContainerViewModel.projectTechStacks.collect {
                    projectTechStackAdapter.submitList(it)
                }
            }

            launch {
                projectWriteContainerViewModel.navigateToMediaPicker.collect {
                    findNavController().safeNavigate(ProjectWritePostFragmentDirections.actionProjectWritePostFragmentToNavMedia(it))
                }
            }

            launch {
                projectWriteContainerViewModel.messageRes.collect {
                    showToast(it)
                }
            }
        }
    }

    private fun initNavBackStackObserve() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.projectWritePostFragment)
        val resultObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!navBackStackEntry.savedStateHandle.contains(KEY_SELECTED_MEDIA_PATHS)) return@LifecycleEventObserver
                val selectedUrls: List<String> = navBackStackEntry.savedStateHandle[KEY_SELECTED_MEDIA_PATHS] ?: emptyList()
                projectWriteContainerViewModel.setProjectBanner(selectedUrls, false)
                navBackStackEntry.savedStateHandle.remove<List<String>>(KEY_SELECTED_MEDIA_PATHS)
            }
        }
        navBackStackEntry.getLifecycle().addObserver(resultObserver)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.getLifecycle().removeObserver(resultObserver)
            }
        })
    }


    override fun onResume() {
        dataBinding.tietPostIntroduction.addTextChangedListener(projectWriteIntroductionTextChangListener)
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        with(dataBinding) {
            tietPostIntroduction.removeTextChangedListener(projectWriteIntroductionTextChangListener)
        }
    }

    override fun onDestroyView() {
        with(dataBinding) {
            tietPostIntroduction.setOnEditorActionListener(null)
            rvBanner.adapter = null
            rvTechStack.adapter = null
        }
        super.onDestroyView()
    }
}
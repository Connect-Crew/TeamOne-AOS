package com.connectcrew.presentation.screen.feature.project.projectintroduction.enrollmentmanagment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.project.enrollmentManagement.ProjectEnrollmentPartManagementAdapter
import com.connectcrew.presentation.databinding.FragmentProjectEnrollmentPartsBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.Const.KEY_PROJECT_ENROLLMENT_REJECT_REASON
import com.connectcrew.presentation.util.MessageType
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.view.createAlert
import com.connectcrew.presentation.util.view.dialogViewBuilder
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectEnrollmentPartsFragment : BaseFragment<FragmentProjectEnrollmentPartsBinding>(R.layout.fragment_project_enrollment_parts) {

    private val projectEnrollmentPartsViewModel: ProjectEnrollmentPartsViewModel by viewModels()

    private val projectEnrollmentPartsAdapter by lazy {
        ProjectEnrollmentPartManagementAdapter(
            onSelectCopyContact = ::contactCopyThenPost,
            onChangePassState = projectEnrollmentPartsViewModel::navigateToUpdateEnrollmentUserStatePopup,
            onSelectedProfile = projectEnrollmentPartsViewModel::navigateToProfile
        )
    }

    private val clipboardManager by lazy {
        requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = projectEnrollmentPartsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
        initNavBackStackObserve()
    }

    private fun initView() {
        dataBinding.rvProjectEnrollPartMember.apply {
            adapter = projectEnrollmentPartsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(RecyclerviewItemDecoration(0, 12, 0, 0, R.layout.item_project_enrollment_part_management))
            setHasFixedSize(true)
        }
    }

    private fun initListener() {
        dataBinding.tlProjectEnrollmentsParts.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectEnrollmentPartsViewModel.projectEnrollmentPartMembers.collect {
                    projectEnrollmentPartsAdapter.submitList(it)
                }
            }

            launch {
                projectEnrollmentPartsViewModel.navigateToPassedPopup.collect { (applyId, nickname) ->
                    createAlert(requireContext())
                        .dialogViewBuilder(
                            title = resources.getString(R.string.project_enrollments_passed_title, nickname),
                            descriptionText = resources.getString(R.string.project_enrollments_passed_description),
                            positiveButtonText = resources.getString(R.string.common_pass),
                            onClickPositiveButton = { projectEnrollmentPartsViewModel.setEnrollmentUserStatePopup(applyId, true) }
                        )
                        .show()
                }
            }

            launch {
                projectEnrollmentPartsViewModel.navigateToRejectPopup.collect { (applyId, nickname) ->
                    createAlert(requireContext())
                        .dialogViewBuilder(
                            iconDrawableRes = R.drawable.ic_warning,
                            iconTint = R.color.color_d62246,
                            title = resources.getString(R.string.project_enrollments_reject_title, nickname),
                            descriptionText = resources.getString(R.string.project_enrollments_reject_description),
                            positiveButtonText = resources.getString(R.string.common_reject),
                            onClickPositiveButton = { findNavController().safeNavigate(ProjectEnrollmentPartsFragmentDirections.actionProjectEnrollmentPartsFragmentToProjectEnrollmentsRejectReasonDialog(applyId)) }
                        )
                        .show()
                }
            }

            launch {
                projectEnrollmentPartsViewModel.navigateToProfile.collect {
                    // TODO:: 프로필로 이동
                }
            }

            launch {
                projectEnrollmentPartsViewModel.loading.collect {
                    if (it) showLoadingDialog() else hideLoadingDialog()
                }
            }

            launch {
                projectEnrollmentPartsViewModel.message.collect {
                    showToast(it)
                }
            }
        }
    }

    private fun initNavBackStackObserve() {
        val navBackStackEntry = findNavController().getBackStackEntry(R.id.projectEnrollmentPartsFragment)
        val resultObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (!navBackStackEntry.savedStateHandle.contains(KEY_PROJECT_ENROLLMENT_REJECT_REASON)) return@LifecycleEventObserver
                val rejectReason: Pair<Int, String> = navBackStackEntry.savedStateHandle[KEY_PROJECT_ENROLLMENT_REJECT_REASON] ?: return@LifecycleEventObserver
                projectEnrollmentPartsViewModel.setEnrollmentUserStatePopup(rejectReason.first, false, rejectReason.second)
                navBackStackEntry.savedStateHandle.remove<Pair<Int, String>>(KEY_PROJECT_ENROLLMENT_REJECT_REASON)
            }
        }
        navBackStackEntry.lifecycle.addObserver(resultObserver)
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                navBackStackEntry.lifecycle.removeObserver(resultObserver)
            }
        })
    }

    private fun contactCopyThenPost(textCopied: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", textCopied))
        showToast(MessageType.ResourceType(R.string.project_enrollments_contact_copy))
    }
}
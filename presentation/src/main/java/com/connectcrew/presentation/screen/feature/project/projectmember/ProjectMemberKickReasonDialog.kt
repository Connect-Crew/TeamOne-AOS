package com.connectcrew.presentation.screen.feature.project.projectmember

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectMemberKickReasonBinding
import com.connectcrew.presentation.model.project.KickReasonType
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.util.Const.KEY_IS_MEMBER_KICKED
import com.connectcrew.presentation.util.hideKeyboard
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.view.createAlert
import com.connectcrew.presentation.util.view.dialogViewBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectMemberKickReasonDialog : BaseAlertDialogFragment<DialogProjectMemberKickReasonBinding>() {

    override val layoutResId: Int = R.layout.dialog_project_member_kick_reason

    private val projectMemberKickReasonViewModel: ProjectMemberKickReasonViewModel by viewModels()

    private val onChangeEtcTextListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = projectMemberKickReasonViewModel.viewModelScope,
            onDebounceEditTextChange = projectMemberKickReasonViewModel::setKickReasonEtc
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(dataBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = projectMemberKickReasonViewModel
        }

        initListener()
        initObserver()
    }

    private fun initListener() {
        with(dataBinding) {
            btnCancel.setOnSingleClickListener { findNavController().navigateUp() }
            cbKickReasonEtc.setOnCheckedChangeListener { checkBox, isChecked ->
                if (isChecked) {
                    if (tietKickReasonEtc.text.toString().isEmpty()) groupWarning.isGone = false
                } else {
                    groupWarning.isGone = true
                }

                tietKickReasonEtc.isFocusable = isChecked
                tietKickReasonEtc.isFocusableInTouchMode = isChecked

                projectMemberKickReasonViewModel.onChangeCheckReason(KickReasonType.TYPE_ETC, isChecked)
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                projectMemberKickReasonViewModel.navigateToProjectContainer.collect { isKicked ->
                    findNavController().run {
                        dataBinding.tietKickReasonEtc.hideKeyboard().also {
                            getBackStackEntry(R.id.projectDetailContainerFragment).savedStateHandle.set(KEY_IS_MEMBER_KICKED, isKicked)
                            createAlert(requireContext())
                                .dialogViewBuilder(
                                    titleRes = R.string.project_detail_member_kick_completed,
                                    descriptionRes = R.string.project_detail_member_kick_completed_description,
                                    isNegativeButtonVisible = false,
                                    onClickPositiveButton = { navigateUp() }
                                ).show()
                        }
                    }
                }
            }

            launch {
                projectMemberKickReasonViewModel.kickReasonEtc.collect { reasonEtc ->
                    if (dataBinding.cbKickReasonEtc.isChecked) {
                        dataBinding.groupWarning.isGone = !reasonEtc.isEmpty()
                    }
                }
            }

            launch {
                projectMemberKickReasonViewModel.loading.collect {
                    if (it) showLoadingDialog() else hideLoadingDialog()
                }
            }

            launch {
                projectMemberKickReasonViewModel.message.collect {
                    showToast(it)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dataBinding.tietKickReasonEtc.addTextChangedListener(onChangeEtcTextListener)
    }

    override fun onPause() {
        super.onPause()
        dataBinding.tietKickReasonEtc.removeTextChangedListener(onChangeEtcTextListener)
    }
}
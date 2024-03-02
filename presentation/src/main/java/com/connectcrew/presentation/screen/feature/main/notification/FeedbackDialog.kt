package com.connectcrew.presentation.screen.feature.main.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogFeedbackBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FeedbackDialog : BaseAlertDialogFragment<DialogFeedbackBinding>() {

    override val layoutResId = R.layout.dialog_feedback

    private val feedbackDialogViewModel: FeedbackDialogViewModel by viewModels()

    private val feedbackTextChangeListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = feedbackDialogViewModel.viewModelScope,
            onDebounceEditTextChange = feedbackDialogViewModel::setFeedbackMessage
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = feedbackDialogViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        initListener()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        dataBinding.tietFeedbackBody.addTextChangedListener(feedbackTextChangeListener)
    }

    override fun onPause() {
        super.onPause()
        dataBinding.tietFeedbackBody.removeTextChangedListener(feedbackTextChangeListener)
    }

    private fun initListener() {
        dataBinding.btnCancel.setOnSingleClickListener { dismiss() }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                feedbackDialogViewModel.navigateToFeedbackCompletedDialog.collect {
                    TODO("피드백 데이터 전송 이후 의견 전송 완료에 대한 Dialog 화면 이동 구현 필요")
                }
            }
        }
    }
}
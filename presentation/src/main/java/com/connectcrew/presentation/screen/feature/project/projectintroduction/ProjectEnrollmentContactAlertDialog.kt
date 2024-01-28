package com.connectcrew.presentation.screen.feature.project.projectintroduction

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.DialogProjectEnrollmentContactBinding
import com.connectcrew.presentation.screen.base.BaseAlertDialogFragment
import com.connectcrew.presentation.util.hideKeyboard
import com.connectcrew.presentation.util.listener.DebounceEditTextListener
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectEnrollmentContactAlertDialog : BaseAlertDialogFragment<DialogProjectEnrollmentContactBinding>() {

    override val layoutResId = R.layout.dialog_project_enrollment_contact

    private val projectEnrollmentViewModel: ProjectEnrollmentViewModel by hiltNavGraphViewModels(R.id.nav_project_enrollment)

    private val projectEnrollContactTextChangeListener by lazy {
        DebounceEditTextListener(
            debouncePeriod = 0L,
            scope = projectEnrollmentViewModel.viewModelScope,
            onDebounceEditTextChange = projectEnrollmentViewModel::setEnrollContact
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        with(dataBinding) {
            btnCancel.setOnSingleClickListener {
                lifecycleScope.launch {
                    dataBinding.tietWriteEnrollmentContact.hideKeyboard().also {
                        delay(100)
                        dismiss()
                    }
                }
            }

            btnEnrollment.setOnSingleClickListener {
                findNavController().navigateUp()
                projectEnrollmentViewModel.setProjectEnrollment()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        dataBinding.tietWriteEnrollmentContact.addTextChangedListener(projectEnrollContactTextChangeListener)
    }

    override fun onPause() {
        super.onPause()
        dataBinding.tietWriteEnrollmentContact.removeTextChangedListener(projectEnrollContactTextChangeListener)
    }
}

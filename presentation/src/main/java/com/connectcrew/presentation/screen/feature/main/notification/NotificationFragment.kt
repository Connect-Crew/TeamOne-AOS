package com.connectcrew.presentation.screen.feature.main.notification

import android.os.Bundle
import android.view.View
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentNotificationBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>(R.layout.fragment_notification) {

    private val notificationViewModel: NotificationViewModel by hiltNavGraphViewModels(R.id.nav_notification)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListener()
    }

    private fun initListener() {
        dataBinding.fabFeedback.setOnSingleClickListener {
            findNavController().safeNavigate(NotificationFragmentDirections.actionNotificationFragmentToFeedbackDialog())
        }
    }
}
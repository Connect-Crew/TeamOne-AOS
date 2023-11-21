package com.connectcrew.presentation.screen.feature.project.projectchat

import android.os.Bundle
import android.view.View
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentProjectDetailChatBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProjectDetailChatFragment: BaseFragment<FragmentProjectDetailChatBinding>(R.layout.fragment_project_detail_chat) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        fun getInstance(): ProjectDetailChatFragment {
            return ProjectDetailChatFragment()
        }
    }
}
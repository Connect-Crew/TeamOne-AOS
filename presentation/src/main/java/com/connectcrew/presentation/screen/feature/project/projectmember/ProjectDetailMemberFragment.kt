package com.connectcrew.presentation.screen.feature.project.projectmember

import android.os.Bundle
import android.view.View
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentProjectDetailMemberBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectDetailMemberFragment : BaseFragment<FragmentProjectDetailMemberBinding>(R.layout.fragment_project_detail_member){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {

        fun getInstance(): ProjectDetailMemberFragment {
            return ProjectDetailMemberFragment()
        }
    }
}
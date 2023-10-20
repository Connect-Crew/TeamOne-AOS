package com.connectcrew.presentation.screen.feature.main.community

import android.os.Bundle
import android.view.View
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentCommunityBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommunityFragment : BaseFragment<FragmentCommunityBinding>(R.layout.fragment_community) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

    }
}
package com.connectcrew.presentation.screen.feature.main.home

import android.os.Bundle
import android.view.View
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentHomeBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

    }
}
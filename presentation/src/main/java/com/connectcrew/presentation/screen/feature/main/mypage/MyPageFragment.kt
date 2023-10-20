package com.connectcrew.presentation.screen.feature.main.mypage

import android.os.Bundle
import android.view.View
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.FragmentMyPageBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {

    }
}
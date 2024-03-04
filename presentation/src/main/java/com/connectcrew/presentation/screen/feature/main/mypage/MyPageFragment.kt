package com.connectcrew.presentation.screen.feature.main.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.project.member.ProjectMemberRepresentProjectAdapter
import com.connectcrew.presentation.databinding.FragmentMyPageBinding
import com.connectcrew.presentation.screen.base.BaseFragment
import com.connectcrew.presentation.util.Const
import com.connectcrew.presentation.util.launchAndRepeatWithViewLifecycle
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.safeNavigate
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyPageFragment : BaseFragment<FragmentMyPageBinding>(R.layout.fragment_my_page) {

    private val myPageViewModel: MyPageViewModel by viewModels()

    private val representProjectAdapter by lazy {
        ProjectMemberRepresentProjectAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.apply {
            viewModel = myPageViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        initView()
        initListener()
        initObserver()
    }

    private fun initView() {
        dataBinding.apply {
            rvUserRepresentProjects.apply {
                adapter = representProjectAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(RecyclerviewItemDecoration(0, 0, 8, 8, R.layout.item_member_represent_project))
                setHasFixedSize(true)
            }
        }
    }

    private fun initListener() {
        with(dataBinding) {
            cvUserProfile.setOnSingleClickListener {
                //::TODO 내 프로필 디테일 화면으로 이동
            }

            llUserProject.setOnSingleClickListener {
                //::TODO 내 프로젝트 목록 화면으로 이동
            }

            llUserResume.setOnSingleClickListener {
                //::TODO 프로젝트 신청 목록 화면으로 이동
            }

            llUserFavoriteProject.setOnSingleClickListener {
                //::TODO 관심 프로젝트 화면으로 이동
            }

            tvSetting.setOnSingleClickListener {
                //::TODO 설정 화면으로 이동
            }

            tvCustomerService.setOnSingleClickListener {
                findNavController().safeNavigate(MyPageFragmentDirections.actionMyPageFragmentToWebViewFragment(Const.URL_TERMS_OF_USE_FOR_CUSTOMER_SERVICE))
            }
        }
    }

    private fun initObserver() {
        launchAndRepeatWithViewLifecycle {
            launch {
                myPageViewModel.representProjects.collect {
                    representProjectAdapter.submitList(it)
                }
            }

            launch {
                myPageViewModel.userParts.collect {
                    //:: TODO 직업 파트 넣기
                }
            }
        }
    }
}
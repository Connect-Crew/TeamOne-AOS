package com.connectcrew.presentation.adapter.project.introduction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.project.introduction.banner.ProjectIntroductionBannerImageAdapter
import com.connectcrew.presentation.databinding.ItemProjectIntroductionBannerBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionDescriptionBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionLeaderBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionRecruitmentNoticeBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionTechStackBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionTitleBinding
import com.connectcrew.presentation.screen.feature.project.projectintroduction.ProjectDetailIntroductionUiModel

class ProjectIntroductionAdapter : ListAdapter<ProjectDetailIntroductionUiModel, RecyclerView.ViewHolder>(
    object : DiffUtil.ItemCallback<ProjectDetailIntroductionUiModel>() {
        override fun areItemsTheSame(oldItem: ProjectDetailIntroductionUiModel, newItem: ProjectDetailIntroductionUiModel): Boolean {
            return (oldItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionBannerUiModel && newItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionBannerUiModel && oldItem.imageUrls == newItem.imageUrls ||
                    oldItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTitleUiModel && newItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTitleUiModel && oldItem.title == newItem.title ||
                    oldItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionLeaderUiModel && newItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionLeaderUiModel && oldItem.leaderInfo.id == newItem.leaderInfo.id ||
                    oldItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionRecruitmentNoticeUiModel && newItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionRecruitmentNoticeUiModel && oldItem.recruitStatus == newItem.recruitStatus ||
                    oldItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionDescriptionUiModel && newItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionDescriptionUiModel && oldItem.projectDescription == newItem.projectDescription ||
                    oldItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTechStack && newItem is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTechStack && oldItem.techStacks == newItem.techStacks)
        }

        override fun areContentsTheSame(oldItem: ProjectDetailIntroductionUiModel, newItem: ProjectDetailIntroductionUiModel): Boolean {
            return oldItem == newItem
        }
    }
) {

    private val recruitmentNoticeViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.item_project_introduction_banner -> ProjectIntroductionBannerViewHolder(ItemProjectIntroductionBannerBinding.inflate(inflater, parent, false))
                .also {
                    it.binding.vpProjectBanner.apply {
                        adapter = ProjectIntroductionBannerImageAdapter()

                        registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                            override fun onPageSelected(position: Int) {
                                super.onPageSelected(position)
                                it.binding.tabBannerImage.apply { selectTab(getTabAt(position), true) }
                            }
                        })
                    }
                }

            R.layout.item_project_introduction_title -> ProjectIntroductionTitleViewHolder(ItemProjectIntroductionTitleBinding.inflate(inflater, parent, false))

            R.layout.item_project_introduction_leader -> ProjectIntroductionLeaderViewHolder(ItemProjectIntroductionLeaderBinding.inflate(inflater, parent, false))

            R.layout.item_project_introduction_recruitment_notice -> ProjectIntroductionRecruitmentNoticeViewHolder(ItemProjectIntroductionRecruitmentNoticeBinding.inflate(inflater, parent, false)).also {
                it.binding.rvMember.apply {
                    layoutManager = LinearLayoutManager(context)
                    setRecycledViewPool(recruitmentNoticeViewPool)
                    setHasFixedSize(true)
                }
            }

            R.layout.item_project_introduction_description -> ProjectIntroductionDescriptionViewHolder(ItemProjectIntroductionDescriptionBinding.inflate(inflater, parent, false))

            R.layout.item_project_introduction_tech_stack -> ProjectIntroductionTechStackViewHolder(ItemProjectIntroductionTechStackBinding.inflate(inflater, parent, false))

            else -> throw IllegalArgumentException("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val uiModel = getItem(position)) {
            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionBannerUiModel -> {
                (holder as ProjectIntroductionBannerViewHolder).bind(uiModel)
            }

            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTitleUiModel -> {
                (holder as ProjectIntroductionTitleViewHolder).bind(uiModel)
            }

            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionLeaderUiModel -> {
                (holder as ProjectIntroductionLeaderViewHolder).bind(uiModel)
            }

            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionRecruitmentNoticeUiModel -> {
                (holder as ProjectIntroductionRecruitmentNoticeViewHolder).bind(uiModel)
            }

            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionDescriptionUiModel -> {
                (holder as ProjectIntroductionDescriptionViewHolder).bind(uiModel)
            }

            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTechStack -> {
                (holder as ProjectIntroductionTechStackViewHolder).bind(uiModel)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionBannerUiModel -> R.layout.item_project_introduction_banner
            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTitleUiModel -> R.layout.item_project_introduction_title
            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionLeaderUiModel -> R.layout.item_project_introduction_leader
            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionRecruitmentNoticeUiModel -> R.layout.item_project_introduction_recruitment_notice
            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionDescriptionUiModel -> R.layout.item_project_introduction_description
            is ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTechStack -> R.layout.item_project_introduction_tech_stack
        }
    }
}
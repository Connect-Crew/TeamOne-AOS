package com.connectcrew.presentation.adapter.project.introduction

import android.annotation.SuppressLint
import android.widget.LinearLayout
import androidx.core.view.children
import com.connectcrew.presentation.R
import com.connectcrew.presentation.adapter.DataBindingViewHolder
import com.connectcrew.presentation.adapter.project.introduction.banner.ProjectIntroductionBannerImageAdapter
import com.connectcrew.presentation.adapter.project.introduction.recruitment.ProjectIntroductionRecruitmentNoticePartAdapter
import com.connectcrew.presentation.databinding.ItemProjectIntroductionBannerBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionDescriptionBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionLeaderBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionRecruitmentNoticeBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionTechStackBinding
import com.connectcrew.presentation.databinding.ItemProjectIntroductionTitleBinding
import com.connectcrew.presentation.screen.feature.project.projectintroduction.ProjectDetailIntroductionUiModel
import com.connectcrew.presentation.util.color
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.tintColor
import com.google.android.material.chip.Chip

class ProjectIntroductionBannerViewHolder(
    val binding: ItemProjectIntroductionBannerBinding
) : DataBindingViewHolder<ItemProjectIntroductionBannerBinding>(binding) {

    fun bind(bannerUiModel: ProjectDetailIntroductionUiModel.ProjectDetailIntroductionBannerUiModel) {
        binding.executeAfter {
            images = bannerUiModel.imageUrls
            (vpProjectBanner.adapter as ProjectIntroductionBannerImageAdapter).submitList(bannerUiModel.imageUrls)
            calculateTabIndicator(this, bannerUiModel.imageUrls.size)
        }
    }

    private fun calculateTabIndicator(binding: ItemProjectIntroductionBannerBinding, indicatorSize: Int) {
        binding.tabBannerImage.let {
            it.removeAllTabs()
            for (index in 0 until indicatorSize) {
                it.addTab(it.newTab())
            }

            it.getChildAt(0).isEnabled = false
            for (index in 0..(it.getChildAt(0) as LinearLayout).childCount) {
                (it.getChildAt(0) as? LinearLayout)?.getChildAt(index)?.isClickable = false
            }
        }
    }
}

class ProjectIntroductionTitleViewHolder(
    val binding: ItemProjectIntroductionTitleBinding
) : DataBindingViewHolder<ItemProjectIntroductionTitleBinding>(binding) {

    fun bind(titleUiModel: ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTitleUiModel) {
        binding.executeAfter {
            this.titleUiModel = titleUiModel

            tvProjectLocation.text = when {
                titleUiModel.isOnline && titleUiModel.region == "미설정" -> root.context.resources.getString(R.string.common_online)
                titleUiModel.isOnline -> root.context.resources.getString(R.string.common_online).plus(", ${titleUiModel.region}")
                else -> titleUiModel.region
            }

            (listOf(titleUiModel.projectState, titleUiModel.careerMin) + titleUiModel.category).mapIndexed { index, item ->
                if (cgProjectTag.children.none { it.tag == item }) {
                    val chip = Chip(cgProjectTag.context, null, R.attr.ProjectFeedChipStyle).apply {
                        tag = item
                        text = item
                        setChipBackgroundColorResource(if (index < 2) R.color.color_f7cdd5 else R.color.color_eeeeee)
                    }
                    cgProjectTag.addView(chip)
                } else {
                    return@executeAfter
                }
            }
        }
    }
}

class ProjectIntroductionLeaderViewHolder(
    val binding: ItemProjectIntroductionLeaderBinding
) : DataBindingViewHolder<ItemProjectIntroductionLeaderBinding>(binding) {

    fun bind(leaderUiModel: ProjectDetailIntroductionUiModel.ProjectDetailIntroductionLeaderUiModel) {
        binding.executeAfter {
            this.leader = leaderUiModel.leaderInfo
            tvLeaderPart.text = leaderUiModel.leaderInfo.parts.firstOrNull()?.value
        }
    }
}

class ProjectIntroductionRecruitmentNoticeViewHolder(
    val binding: ItemProjectIntroductionRecruitmentNoticeBinding
) : DataBindingViewHolder<ItemProjectIntroductionRecruitmentNoticeBinding>(binding) {

    @SuppressLint("SetTextI18n")
    fun bind(recruitmentNoticeUiModel: ProjectDetailIntroductionUiModel.ProjectDetailIntroductionRecruitmentNoticeUiModel) {
        binding.executeAfter {
            val recruitmentColor = if (recruitmentNoticeUiModel.isEnroll) R.color.color_00aee4 else R.color.color_9e9e9e

            ivMember.imageTintList = ivMember.context.tintColor(recruitmentColor)
            tvMember.setTextColor(tvMember.context.color(recruitmentColor))
            tvMemberTotalCount.apply {
                text = "${recruitmentNoticeUiModel.totalCurrentCount} / ${recruitmentNoticeUiModel.totalMaxCount}"
                setTextColor(context.color(recruitmentColor))
            }

            rvMember.adapter = ProjectIntroductionRecruitmentNoticePartAdapter(recruitmentNoticeUiModel.recruitStatus)
        }
    }
}

class ProjectIntroductionDescriptionViewHolder(
    val binding: ItemProjectIntroductionDescriptionBinding
) : DataBindingViewHolder<ItemProjectIntroductionDescriptionBinding>(binding) {

    fun bind(descriptionUiModel: ProjectDetailIntroductionUiModel.ProjectDetailIntroductionDescriptionUiModel) {
        binding.executeAfter {
            description = descriptionUiModel.projectDescription
        }
    }
}

class ProjectIntroductionTechStackViewHolder(
    val binding: ItemProjectIntroductionTechStackBinding
) : DataBindingViewHolder<ItemProjectIntroductionTechStackBinding>(binding) {

    fun bind(techStackUiModel: ProjectDetailIntroductionUiModel.ProjectDetailIntroductionTechStack) {
        binding.executeAfter {
            techStackUiModel.techStacks.map { item ->
                if (cgTechStack.children.none { it.tag == item }) {
                    val chip = Chip(cgTechStack.context, null, R.attr.ProjectTechStackChipStyle).apply {
                        tag = item
                        text = item
                    }
                    cgTechStack.addView(chip)
                } else {
                    return@executeAfter
                }
            }
        }
    }
}
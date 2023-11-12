package com.connectcrew.presentation.adapter.home.project.member

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectFeedMemberBinding
import com.connectcrew.presentation.databinding.ItemProjectFeedMemberHeaderBinding
import com.connectcrew.presentation.model.project.RecruitStatusUiModel
import com.connectcrew.presentation.util.executeAfter

class ProjectFeedMemberSpinnerAdapter(
    context: Context,
    private val members: List<RecruitStatusUiModel>
) : ArrayAdapter<RecruitStatusUiModel>(context, R.layout.item_project_feed_member, members) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = ItemProjectFeedMemberHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        (members.first() as? RecruitStatusUiModel.TotalMemberRecruitStatusUiModel)?.let {
            binding.executeAfter { tvMemberCount.text = "${it.currentCount} / ${it.maxCount}" }
        }

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = when (position) {
            0 -> ItemProjectFeedMemberHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            else -> ItemProjectFeedMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }

        when (binding) {
            is ItemProjectFeedMemberHeaderBinding -> binding.executeAfter {
                val item = (getItem(position) as RecruitStatusUiModel.TotalMemberRecruitStatusUiModel)
                tvMemberCount.text = "${item.currentCount} / ${item.maxCount}"
                ivMemberCountArrow.rotation = 180f
            }

            is ItemProjectFeedMemberBinding -> binding.executeAfter {
                val item = (getItem(position) as RecruitStatusUiModel.PartMembersRecruitStatusUiModel).recruitStatus
                tvMemberPart.text = item.part
                tvMemberCount.text = context.getString(R.string.unit_member, (item.maxCount - item.currentCount))
            }
        }

        return binding.root
    }

    override fun getCount(): Int = members.size

    override fun getItem(position: Int) = members[position]

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecruitStatusUiModel.TotalMemberRecruitStatusUiModel -> R.layout.item_project_feed_member_header
            else -> R.layout.item_project_feed_member
        }
    }
}
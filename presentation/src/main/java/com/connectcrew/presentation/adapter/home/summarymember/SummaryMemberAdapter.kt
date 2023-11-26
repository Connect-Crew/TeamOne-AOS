package com.connectcrew.presentation.adapter.home.summarymember

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemSummaryMemberBinding
import com.connectcrew.presentation.model.project.RecruitStatus
import com.connectcrew.presentation.util.executeAfter

class SummaryMemberAdapter : ListAdapter<RecruitStatus, SummaryMemberViewHolder>(
    object : DiffUtil.ItemCallback<RecruitStatus>() {
        override fun areItemsTheSame(oldItem: RecruitStatus, newItem: RecruitStatus): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: RecruitStatus, newItem: RecruitStatus): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SummaryMemberViewHolder(ItemSummaryMemberBinding.inflate(inflater, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SummaryMemberViewHolder, position: Int) {
        val recruitStatus = getItem(position) ?: return
        holder.binding.executeAfter {
            tvPart.apply {
                text = recruitStatus.part
                setTextColor(ContextCompat.getColor(tvPart.context, if (recruitStatus.isEnroll) R.color.color_616161 else R.color.color_9e9e9e))
            }
            tvMemberCount.apply {
                text = "${recruitStatus.currentCount} / ${recruitStatus.maxCount}"
                setTextColor(ContextCompat.getColor(tvPart.context, if (recruitStatus.isEnroll) R.color.color_d62246 else R.color.color_9e9e9e))
            }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_summary_member
}
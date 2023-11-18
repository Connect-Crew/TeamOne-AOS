package com.connectcrew.presentation.adapter.project.introduction.recruitment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectIntroductionRecruitmentNoticePartBinding
import com.connectcrew.presentation.model.project.RecruitStatus
import com.connectcrew.presentation.util.executeAfter

class ProjectIntroductionRecruitmentNoticePartAdapter(
    private val recruitStatus: List<RecruitStatus>
) : RecyclerView.Adapter<ProjectIntroductionRecruitmentNoticePartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectIntroductionRecruitmentNoticePartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectIntroductionRecruitmentNoticePartViewHolder(ItemProjectIntroductionRecruitmentNoticePartBinding.inflate(inflater, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProjectIntroductionRecruitmentNoticePartViewHolder, position: Int) {
        val item = recruitStatus[position]
        holder.binding.executeAfter {
            val context = this.root.context
            val isEnrollEnable = (item.maxCount - item.currentCount) > 0

            llPartInfo.setBackgroundColor(ContextCompat.getColor(context, if (isEnrollEnable) R.color.color_f1fcff else android.R.color.white))

            tvPart.apply {
                text = item.category
                setTextColor(ContextCompat.getColor(context, if (isEnrollEnable) R.color.color_00aee4 else R.color.color_9e9e9e))
            }

            tvSubPart.apply {
                text = item.part
                setTextColor(ContextCompat.getColor(context, if (isEnrollEnable) R.color.color_616161 else R.color.color_9e9e9e))
            }

            tvPartCount.apply {
                text = "${item.currentCount} / ${item.maxCount}"
                setTextColor(ContextCompat.getColor(context, if (isEnrollEnable) R.color.color_d62246 else R.color.color_9e9e9e))
            }
        }
    }

    override fun getItemCount() = recruitStatus.size

    override fun getItemViewType(position: Int) = R.layout.item_project_introduction_recruitment_notice_part
}
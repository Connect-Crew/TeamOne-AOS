package com.connectcrew.presentation.adapter.project.enrollment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectDetailIntroductionEnrollmentBinding
import com.connectcrew.presentation.model.project.RecruitStatus
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class ProjectEnrollmentAdapter(
    private val onSelectedPart: (RecruitStatus) -> Unit
) : ListAdapter<RecruitStatus, ProjectEnrollmentViewHolder>(
    object : DiffUtil.ItemCallback<RecruitStatus>() {
        override fun areItemsTheSame(oldItem: RecruitStatus, newItem: RecruitStatus): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: RecruitStatus, newItem: RecruitStatus): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectEnrollmentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectEnrollmentViewHolder(ItemProjectDetailIntroductionEnrollmentBinding.inflate(inflater, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProjectEnrollmentViewHolder, position: Int) {
        val recruitStatus = getItem(position) ?: return

        holder.binding.executeAfter {
            val isEnable = recruitStatus.isEnroll && !recruitStatus.isApplied

            cvProjectEnroll.apply {
                strokeColor = ContextCompat.getColor(cvProjectEnroll.context, if (isEnable) R.color.color_00aee4 else R.color.color_9e9e9e)
                setCardBackgroundColor(ContextCompat.getColor(cvProjectEnroll.context, if (isEnable) R.color.color_f1fcff else android.R.color.white))
            }

            tvMemberCategory.apply {
                text = recruitStatus.category
                setTextColor(ContextCompat.getColor(context, if (isEnable) R.color.color_424242 else R.color.color_9e9e9e))
            }
            tvMemberPart.apply {
                text = recruitStatus.part
                setTextColor(ContextCompat.getColor(context, if (isEnable) R.color.color_00aee4 else R.color.color_9e9e9e))
            }
            tvMemberCount.apply {
                text = "${recruitStatus.currentCount} / ${recruitStatus.maxCount}"
                setTextColor(ContextCompat.getColor(context, if (isEnable) R.color.color_d62246 else R.color.color_9e9e9e))
            }

            tvMemberDescription.text = recruitStatus.comment

            btnEnroll.apply {
                text = resources.getString(
                    when {
                        recruitStatus.isApplied -> R.string.project_detail_introduction_enroll_completed
                        recruitStatus.isEnroll -> R.string.project_detail_introduction_enroll
                        else -> R.string.project_detail_introduction_enroll_disable_short
                    }
                )
                isEnabled = isEnable
                setOnSingleClickListener { onSelectedPart(recruitStatus) }
            }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_detail_introduction_enrollment
}
package com.connectcrew.presentation.adapter.project.enrollmentManagement

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectEnrollmentManagementBinding
import com.connectcrew.presentation.model.project.ProjectEnrollmentMember
import com.connectcrew.presentation.util.color
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.tintColor

class ProjectEnrollmentManagementAdapter(
    private val onSelectedEnrollmentPart: (ProjectEnrollmentMember) -> Unit
) : ListAdapter<ProjectEnrollmentMember, ProjectEnrollmentManagementViewHolder>(
    object : DiffUtil.ItemCallback<ProjectEnrollmentMember>() {
        override fun areItemsTheSame(oldItem: ProjectEnrollmentMember, newItem: ProjectEnrollmentMember): Boolean {
            return oldItem.part == newItem.part
        }

        override fun areContentsTheSame(oldItem: ProjectEnrollmentMember, newItem: ProjectEnrollmentMember): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectEnrollmentManagementViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectEnrollmentManagementViewHolder(ItemProjectEnrollmentManagementBinding.inflate(inflater, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProjectEnrollmentManagementViewHolder, position: Int) {
        val projectEnrollmentMember = getItem(position) ?: return

        holder.binding.executeAfter {
            val isEnable = projectEnrollmentMember.isEnroll

            cvProjectEnroll.apply {
                strokeColor = context.color(if (isEnable) R.color.color_00aee4 else R.color.color_9e9e9e)
                setCardBackgroundColor(context.color(if (isEnable) R.color.color_f1fcff else R.color.color_ffffff))
            }

            tvMemberCategory.apply {
                text = projectEnrollmentMember.category
                setTextColor(context.color(if (isEnable) R.color.color_00aee4 else R.color.color_9e9e9e))
            }
            tvMemberPart.apply {
                text = projectEnrollmentMember.part
                setTextColor(context.color(if (isEnable) R.color.color_424242 else R.color.color_9e9e9e))
            }
            tvMemberCount.apply {
                text = "${projectEnrollmentMember.currentCount} / ${projectEnrollmentMember.maxCount}"
                setTextColor(context.color(if (isEnable) R.color.color_d62246 else R.color.color_9e9e9e))
            }

            tvMemberDescription.text = projectEnrollmentMember.comment ?: ""

            ivNext.apply {
                imageTintList = context.tintColor(if (isEnable) R.color.color_00aee4 else R.color.color_9e9e9e)
            }

            root.setOnSingleClickListener {
                onSelectedEnrollmentPart(projectEnrollmentMember)
            }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_enrollment_management
}
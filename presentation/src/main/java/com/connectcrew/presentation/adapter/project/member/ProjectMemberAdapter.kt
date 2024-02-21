package com.connectcrew.presentation.adapter.project.member

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectMemberBinding
import com.connectcrew.presentation.model.project.ProjectMember
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener
import com.connectcrew.presentation.util.widget.RecyclerviewItemDecoration

class ProjectMemberAdapter(
    private val onClickMemberProfile: (ProjectMember) -> Unit,
    private val onClickKickMember: (ProjectMember) -> Unit,
    private val onClickRepresentProject: (Long) -> Unit
) : ListAdapter<ProjectMember, ProjectMemberViewHolder>(
    object : DiffUtil.ItemCallback<ProjectMember>() {
        override fun areItemsTheSame(oldItem: ProjectMember, newItem: ProjectMember): Boolean {
            return oldItem.profile.id == newItem.profile.id
        }

        override fun areContentsTheSame(oldItem: ProjectMember, newItem: ProjectMember): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectMemberViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectMemberViewHolder(ItemProjectMemberBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectMemberViewHolder, position: Int) {
        val data = getItem(position) ?: return

        holder.binding.executeAfter {
            member = data

            tvMemberPart.text = data.parts.joinToString(",")

            with(rvMemberRepresentProjects) {
                adapter = ProjectMemberRepresentProjectAdapter(onClickRepresentProject).apply { submitList(data.profile.representProjects) }
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(RecyclerviewItemDecoration(0, 0, 8, 8, R.layout.item_member_represent_project))
                setHasFixedSize(true)
            }

            ivNavigateProfile.setOnSingleClickListener { onClickMemberProfile(data) }
            btnMemberKick.setOnSingleClickListener { onClickKickMember(data) }
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_project_member
}
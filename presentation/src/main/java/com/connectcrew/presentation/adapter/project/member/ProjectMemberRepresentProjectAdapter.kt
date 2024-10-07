package com.connectcrew.presentation.adapter.project.member

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemMemberRepresentProjectBinding
import com.connectcrew.presentation.model.project.RepresentProject
import com.connectcrew.presentation.util.executeAfter

class ProjectMemberRepresentProjectAdapter : ListAdapter<RepresentProject, ProjectMemberRepresentProjectViewholder>(
    object : DiffUtil.ItemCallback<RepresentProject>() {
        override fun areItemsTheSame(oldItem: RepresentProject, newItem: RepresentProject): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RepresentProject, newItem: RepresentProject): Boolean {
            return oldItem == newItem
        }
    }
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectMemberRepresentProjectViewholder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectMemberRepresentProjectViewholder(ItemMemberRepresentProjectBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectMemberRepresentProjectViewholder, position: Int) {
        val data = getItem(position) ?: return
        holder.binding.executeAfter {
            thumbnail = data.thumbnailUrl
        }
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_member_represent_project
}
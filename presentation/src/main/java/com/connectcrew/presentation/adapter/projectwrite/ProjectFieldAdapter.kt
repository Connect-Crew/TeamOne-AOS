package com.connectcrew.presentation.adapter.projectwrite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectWriteFieldBinding
import com.connectcrew.presentation.model.project.ProjectFieldInfo

class ProjectFieldAdapter(
    private val selectedProjectField: (ProjectFieldInfo) -> Unit
) : ListAdapter<ProjectFieldInfo, ProjectFieldViewHolder>(
    object : DiffUtil.ItemCallback<ProjectFieldInfo>() {
        override fun areItemsTheSame(oldItem: ProjectFieldInfo, newItem: ProjectFieldInfo): Boolean {
            return oldItem.category.key == newItem.category.key
        }

        override fun areContentsTheSame(oldItem: ProjectFieldInfo, newItem: ProjectFieldInfo): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectFieldViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectFieldViewHolder(ItemProjectWriteFieldBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectFieldViewHolder, position: Int) {
        val projectFieldItem = getItem(position) ?: return
        holder.bind(projectFieldItem, selectedProjectField)
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_write_field
}
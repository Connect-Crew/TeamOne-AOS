package com.connectcrew.presentation.adapter.projectwrite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectWriteLocationBinding
import com.connectcrew.presentation.model.project.ProjectInfo
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class ProjectLocationAdapter(
    private val onSelectLocation: (ProjectInfo) -> Unit
) : ListAdapter<ProjectInfo, ProjectLocationViewHolder>(
    object : DiffUtil.ItemCallback<ProjectInfo>() {
        override fun areItemsTheSame(oldItem: ProjectInfo, newItem: ProjectInfo): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: ProjectInfo, newItem: ProjectInfo): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectLocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectLocationViewHolder(ItemProjectWriteLocationBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectLocationViewHolder, position: Int) {
        val regionItem = getItem(position) ?: return

        holder.binding.executeAfter {
            region = regionItem
            root.setOnSingleClickListener { onSelectLocation(regionItem) }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_write_location
}
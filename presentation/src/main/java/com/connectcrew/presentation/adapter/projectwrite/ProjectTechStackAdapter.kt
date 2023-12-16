package com.connectcrew.presentation.adapter.projectwrite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectWriteTechStackBinding
import com.connectcrew.presentation.util.executeAfter
import com.connectcrew.presentation.util.listener.setOnSingleClickListener

class ProjectTechStackAdapter(
    private val onRemoveProjectTechStack: (String) -> Unit
) : ListAdapter<String, ProjectTechStackViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectTechStackViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectTechStackViewHolder(ItemProjectWriteTechStackBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectTechStackViewHolder, position: Int) {
        val techStackText = getItem(position) ?: return

        holder.binding.executeAfter {
            techStack = techStackText
            root.setOnSingleClickListener {
                onRemoveProjectTechStack(techStackText)
            }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_write_tech_stack
}
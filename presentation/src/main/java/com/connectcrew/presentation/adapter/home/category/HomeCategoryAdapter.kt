package com.connectcrew.presentation.adapter.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectCategoryBinding
import com.connectcrew.presentation.model.project.ProjectCategoryItem

class HomeCategoryAdapter(
    private val onSelectedCategory: (ProjectCategoryItem) -> Unit
) : ListAdapter<ProjectCategoryItem, HomeCategoryViewHolder>(
    object : DiffUtil.ItemCallback<ProjectCategoryItem>() {
        override fun areItemsTheSame(oldItem: ProjectCategoryItem, newItem: ProjectCategoryItem): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(oldItem: ProjectCategoryItem, newItem: ProjectCategoryItem): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeCategoryViewHolder(ItemProjectCategoryBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeCategoryViewHolder, position: Int) {
        val category = getItem(position) ?: return
        holder.bind(category, onSelectedCategory)
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_category

}
package com.connectcrew.presentation.adapter.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectCategoryBinding
import com.connectcrew.presentation.model.project.ProjectCategoryType

class HomeCategoryAdapter : ListAdapter<ProjectCategoryType, HomeCategoryViewHolder>(
    object : DiffUtil.ItemCallback<ProjectCategoryType>() {
        override fun areItemsTheSame(oldItem: ProjectCategoryType, newItem: ProjectCategoryType): Boolean {
            return oldItem.type == newItem.type
        }

        override fun areContentsTheSame(oldItem: ProjectCategoryType, newItem: ProjectCategoryType): Boolean {
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
        when (category) {
            ProjectCategoryType.CATEGORY_ALL -> Pair(R.drawable.ic_category_all, R.string.category_all)
            ProjectCategoryType.CATEGORY_DEVELOP -> Pair(R.drawable.ic_category_develop, R.string.category_develop)
            ProjectCategoryType.CATEGORY_PLAN -> Pair(R.drawable.ic_category_planner, R.string.category_plan)
            ProjectCategoryType.CATEGORY_DESIGN -> Pair(R.drawable.ic_category_designer, R.string.category_design)
            ProjectCategoryType.CATEGORY_MARKETING -> Pair(R.drawable.ic_category_marketing, R.string.category_marketing)
            ProjectCategoryType.CATEGORY_SALES -> Pair(R.drawable.ic_category_sales, R.string.category_sales)
            ProjectCategoryType.CATEGORY_CS -> Pair(R.drawable.ic_category_cs, R.string.category_cs)
            ProjectCategoryType.CATEGORY_PROFESSIONAL -> Pair(R.drawable.ic_category_professional, R.string.category_professional)
            ProjectCategoryType.CATEGORY_ENGINEERING -> Pair(R.drawable.ic_category_engineering, R.string.category_engineering)
            ProjectCategoryType.CATEGORY_MEDIA -> Pair(R.drawable.ic_category_media, R.string.category_media)
            ProjectCategoryType.CATEGORY_ETC -> Pair(R.drawable.ic_category_etc, R.string.category_etc)
        }.let { (categoryImg, categoryText) ->
            holder.bind(categoryImg, categoryText)
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_category

}
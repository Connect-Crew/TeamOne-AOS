package com.connectcrew.presentation.adapter.project.introduction.banner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectIntroductionBannerImageBinding
import com.connectcrew.presentation.util.executeAfter

class ProjectIntroductionBannerImageAdapter : ListAdapter<String, ProjectIntroductionBannerImageViewHolder>(
    object : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectIntroductionBannerImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectIntroductionBannerImageViewHolder(ItemProjectIntroductionBannerImageBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectIntroductionBannerImageViewHolder, position: Int) {
        val imageUrl = getItem(position) ?: return
        holder.binding.executeAfter { this.imageUrl = imageUrl }
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_introduction_banner_image
}
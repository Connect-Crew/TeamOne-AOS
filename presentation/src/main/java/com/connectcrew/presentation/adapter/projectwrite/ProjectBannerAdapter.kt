package com.connectcrew.presentation.adapter.projectwrite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectWriteBannerBinding
import com.connectcrew.presentation.util.executeAfter

class ProjectBannerAdapter(
    private val removeProjectBanner: (String) -> Unit
) : ListAdapter<String, ProjectBannerViewHolder>(
    object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectBannerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProjectBannerViewHolder(ItemProjectWriteBannerBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ProjectBannerViewHolder, position: Int) {
        val bannerItem = getItem(position) ?: return

        holder.binding.executeAfter {
            imageUrl = bannerItem
            ivDelete.setOnClickListener { removeProjectBanner(bannerItem) }
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_project_write_banner
}
package com.connectcrew.presentation.adapter.home.project

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.connectcrew.presentation.R
import com.connectcrew.presentation.databinding.ItemProjectFeedBinding
import com.connectcrew.presentation.model.project.ProjectFeed

class HomeProjectFeedAdapter : PagingDataAdapter<ProjectFeed, HomeProjectFeedViewHolder>(
    object : DiffUtil.ItemCallback<ProjectFeed>() {
        override fun areItemsTheSame(oldItem: ProjectFeed, newItem: ProjectFeed): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProjectFeed, newItem: ProjectFeed): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeProjectFeedViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HomeProjectFeedViewHolder(ItemProjectFeedBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: HomeProjectFeedViewHolder, position: Int) {
        //:: TODO 화면 작성
    }

    override fun getItemViewType(position: Int): Int = R.layout.item_project_feed
}